package app.fractial.schema.commands.arguments;

import app.fractial.schema.core.registries.SchemaRegistries;
import app.fractial.schema.mixin.core.registries.RegistriesMixin;
import app.fractial.schema.world.item.ItemStackManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.stream.Stream;

import static net.minecraft.commands.arguments.ResourceKeyArgument.getRegistryKey;

public class SchemaResourceKeyArgument {
    private static final DynamicCommandExceptionType ERROR_INVALID_ITEM_STACK = new DynamicCommandExceptionType(
            object -> Component.translatableEscape("item.notFound", object)
    );

    public static ItemStack getItemStack(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ResourceKey<ItemStack> resourceKey = getRegistryKey(commandContext, string, RegistriesMixin.createRegistryKey(SchemaRegistries.ITEM_STACK.location().getPath()), ERROR_INVALID_ITEM_STACK);
        return ItemStackManager.getItemStack(resourceKey.location());
    }

    public static Stream<String> getItemStackManager() throws CommandSyntaxException {
        return ItemStackManager.getItemStackManager().keySet().stream().map(ResourceLocation::toString);
    }
}
