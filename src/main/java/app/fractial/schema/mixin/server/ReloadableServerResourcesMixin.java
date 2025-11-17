package app.fractial.schema.mixin.server;

import app.fractial.schema.world.item.ItemStackManager;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    @Unique
    private ItemStackManager items;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;<init>(Lnet/minecraft/core/HolderLookup$Provider;)V", shift = At.Shift.BEFORE))
    private void init(LayeredRegistryAccess layeredRegistryAccess, HolderLookup.Provider provider, FeatureFlagSet featureFlagSet, Commands.CommandSelection commandSelection, List list, int i, CallbackInfo ci) {
        this.items = new ItemStackManager(provider);
    }

    @Inject(method = "listeners", at = @At("RETURN"), cancellable = true)
    public void listeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir) {
        List<PreparableReloadListener> list = cir.getReturnValue();
        List<PreparableReloadListener> newList = new ArrayList<>(list);
        newList.addFirst(this.items);
        cir.setReturnValue(Collections.unmodifiableList(newList));

    }
}
