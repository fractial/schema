package app.fractial.schema.core.registries;

import app.fractial.schema.mixin.core.registries.RegistriesMixin;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public class SchemaRegistries {
    public static final ResourceKey<Registry<ItemStack>> ITEM_STACK = RegistriesMixin.createRegistryKey("item_stack");
}
