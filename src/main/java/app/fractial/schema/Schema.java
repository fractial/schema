package app.fractial.schema;

import net.fabricmc.api.ModInitializer;

import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schema implements ModInitializer {
	public static final String MOD_ID = "schema";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {}
}