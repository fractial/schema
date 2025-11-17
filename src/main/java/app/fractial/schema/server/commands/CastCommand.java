package app.fractial.schema.server.commands;

import app.fractial.schema.commands.arguments.SchemaResourceKeyArgument;
import app.fractial.schema.core.registries.SchemaRegistries;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class CastCommand {
    private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((object, object2) -> Component.translatableEscape("arguments.item.overstacked", object, object2));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("cast").requires(Commands.hasPermission(2))).then(Commands.argument("targets", EntityArgument.players()).then(((RequiredArgumentBuilder) Commands.argument("item", ResourceKeyArgument.key(SchemaRegistries.ITEM_STACK)).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(SchemaResourceKeyArgument.getItemStackManager(), suggestionsBuilder)).executes((commandContext) -> giveItem(commandContext.getSource(), SchemaResourceKeyArgument.getItemStack(commandContext, "item"), EntityArgument.getPlayers(commandContext, "targets"), 1))).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes((commandContext) -> giveItem(commandContext.getSource(), SchemaResourceKeyArgument.getItemStack(commandContext, "item"), EntityArgument.getPlayers(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "count")))))));
    }

    public static ItemStack createItemStack(ItemStack itemStack, int count, boolean allowOversizedStacks) throws CommandSyntaxException {
        if (allowOversizedStacks && count > itemStack.getMaxStackSize()) {
            throw ERROR_STACK_TOO_BIG.create(itemStack.getItemName(), itemStack.getMaxStackSize());
        } else {
            itemStack.setCount(count);
            return itemStack;
        }
    }

    private static int giveItem(CommandSourceStack commandSourceStack, ItemStack itemInput, Collection<ServerPlayer> collection, int i) throws CommandSyntaxException {
        System.out.println("DEBUG GIVE");
        ItemStack itemStack = createItemStack(itemInput, 1, false);
        int j = itemStack.getMaxStackSize();
        int k = j * 100;
        if (i > k) {
            commandSourceStack.sendFailure(Component.translatable("commands.give.failed.toomanyitems", k, itemStack.getDisplayName()));
            return 0;
        } else {
            for (ServerPlayer serverPlayer : collection) {
                int l = i;

                while (l > 0) {
                    int m = Math.min(j, l);
                    l -= m;
                    ItemStack itemStack2 = createItemStack(itemInput, m, false);
                    boolean bl = serverPlayer.getInventory().add(itemStack2);
                    if (bl && itemStack2.isEmpty()) {
                        ItemEntity itemEntity = serverPlayer.drop(itemStack, false);
                        if (itemEntity != null) {
                            itemEntity.makeFakeItem();
                        }

                        serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        serverPlayer.containerMenu.broadcastChanges();
                    } else {
                        ItemEntity itemEntity = serverPlayer.drop(itemStack2, false);
                        if (itemEntity != null) {
                            itemEntity.setNoPickUpDelay();
                            itemEntity.setTarget(serverPlayer.getUUID());
                        }
                    }
                }
            }

            if (collection.size() == 1) {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.give.success.single", i, itemStack.getDisplayName(), collection.iterator().next().getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.give.success.single", i, itemStack.getDisplayName(), collection.size()), true);
            }

            return collection.size();
        }
    }
}
