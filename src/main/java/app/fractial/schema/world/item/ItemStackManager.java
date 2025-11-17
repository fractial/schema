package app.fractial.schema.world.item;

import app.fractial.schema.core.registries.SchemaRegistries;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ItemStackManager extends SimplePreparableReloadListener<Map<ResourceLocation, ItemStack>> {
    private static final FileToIdConverter ITEM_STACK_LISTER = FileToIdConverter.registry(SchemaRegistries.ITEM_STACK);
    private final HolderLookup.Provider registries;
    public static Map<ResourceLocation, ItemStack> items = Map.of();

    public ItemStackManager(HolderLookup.Provider provider) {
        this.registries = provider;
    }

    @Override
    protected @NotNull Map<ResourceLocation, ItemStack> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        SortedMap<ResourceLocation, ItemStack> map = new TreeMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(
                resourceManager,
                ITEM_STACK_LISTER,
                this.registries.createSerializationContext(JsonOps.INSTANCE),
                ItemStack.CODEC,
                map
        );
        items = map;
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, ItemStack> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        items = ImmutableMap.copyOf(map);
    }
}
