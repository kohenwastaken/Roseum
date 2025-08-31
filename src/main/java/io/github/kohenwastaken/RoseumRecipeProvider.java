package io.github.kohenwastaken;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RoseumRecipeProvider extends FabricRecipeProvider {
    public RoseumRecipeProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        // ---------- NUGGET <-> INGOT ----------
        // 9 nugget -> 1 ingot (shaped)
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, RoseumItems.ROSEGOLD_INGOT)
                .pattern("###").pattern("###").pattern("###")
                .input('#', RoseumItems.ROSEGOLD_NUGGET)
                .criterion("has_nugget", conditionsFromItem(RoseumItems.ROSEGOLD_NUGGET))
                .offerTo(exporter, Roseum.id("rosegold_ingot_from_nuggets"));

        // 1 ingot -> 9 nugget (shapeless)
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RoseumItems.ROSEGOLD_NUGGET, 9)
                .input(RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_nugget_from_ingot"));


        // ---------- INGOT <-> BLOCK ----------
        // 9 ingot -> 1 block (shaped)
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, RoseumBlocks.ROSEGOLD_BLOCK)
                .pattern("###").pattern("###").pattern("###")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_block_from_ingots"));

        // 1 block -> 9 ingot (shapeless)
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RoseumItems.ROSEGOLD_INGOT, 9)
                .input(RoseumBlocks.ROSEGOLD_BLOCK)
                .criterion("has_block", conditionsFromItem(RoseumBlocks.ROSEGOLD_BLOCK))
                .offerTo(exporter, Roseum.id("rosegold_ingot_from_block"));

     // --- DOOR (6 ingot -> 3 door), TRAPDOOR (4 ingot -> 1 trapdoor) ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RoseumBlocks.ROSEGOLD_DOOR, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_door"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RoseumBlocks.ROSEGOLD_TRAPDOOR, 1)
                .pattern("##")
                .pattern("##")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_trapdoor"));

        // ---------- RAW <-> RAW BLOCK ----------
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, RoseumBlocks.RAW_ROSEGOLD_BLOCK)
                .pattern("###").pattern("###").pattern("###")
                .input('#', RoseumItems.RAW_ROSEGOLD)
                .criterion("has_raw", conditionsFromItem(RoseumItems.RAW_ROSEGOLD))
                .offerTo(exporter, Roseum.id("raw_rosegold_block_from_raw"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, RoseumItems.RAW_ROSEGOLD, 9)
                .input(RoseumBlocks.RAW_ROSEGOLD_BLOCK)
                .criterion("has_raw_block", conditionsFromItem(RoseumBlocks.RAW_ROSEGOLD_BLOCK))
                .offerTo(exporter, Roseum.id("raw_rosegold_from_block"));
        
        // --- FURNACE: raw -> ingot (smelting & blasting) ---
        java.util.List<ItemConvertible> smeltInputs = java.util.List.of(RoseumItems.RAW_ROSEGOLD);
        offerSmelting(exporter, smeltInputs, RecipeCategory.MISC, RoseumItems.ROSEGOLD_INGOT, 0.7f, 200, "rosegold_ingot");
        offerBlasting(exporter, smeltInputs, RecipeCategory.MISC, RoseumItems.ROSEGOLD_INGOT, 0.7f, 100, "rosegold_ingot");

        // --- TOOLS ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RoseumItems.ROSEGOLD_PICKAXE)
                .pattern("###").pattern(" X ").pattern(" X ")
                .input('#', RoseumItems.ROSEGOLD_INGOT).input('X', Items.STICK)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_pickaxe"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RoseumItems.ROSEGOLD_AXE)
                .pattern("##").pattern("#X").pattern(" X")
                .input('#', RoseumItems.ROSEGOLD_INGOT).input('X', Items.STICK)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_axe"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RoseumItems.ROSEGOLD_SHOVEL)
                .pattern("#").pattern("X").pattern("X")
                .input('#', RoseumItems.ROSEGOLD_INGOT).input('X', Items.STICK)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_shovel"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, RoseumItems.ROSEGOLD_HOE)
                .pattern("##").pattern(" X").pattern(" X")
                .input('#', RoseumItems.ROSEGOLD_INGOT).input('X', Items.STICK)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_hoe"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RoseumItems.ROSEGOLD_SWORD)
                .pattern("#").pattern("#").pattern("X")
                .input('#', RoseumItems.ROSEGOLD_INGOT).input('X', Items.STICK)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_sword"));

     // --- ARMOR ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RoseumItems.ROSEGOLD_HELMET)
                .pattern("###").pattern("# #")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_helmet"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RoseumItems.ROSEGOLD_CHESTPLATE)
                .pattern("# #").pattern("###").pattern("###")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_chestplate"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RoseumItems.ROSEGOLD_LEGGINGS)
                .pattern("###").pattern("# #").pattern("# #")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_leggings"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, RoseumItems.ROSEGOLD_BOOTS)
                .pattern("# #").pattern("# #")
                .input('#', RoseumItems.ROSEGOLD_INGOT)
                .criterion("has_ingot", conditionsFromItem(RoseumItems.ROSEGOLD_INGOT))
                .offerTo(exporter, Roseum.id("rosegold_boots"));
        
        
     // === ROSEGOLD ALLOY: rules-based generation ===
        for (var mode : RoseumConfig.Mode.values()) {
            for (int c = 1; c <= 4; c++) {
                final int count = c;

                int copper = switch (mode) { case C3_G1 -> 3; case C2_G2 -> 2; case C1_G3 -> 1; };
                int gold   = 4 - copper;

                // IDs locale-safe
                final String modeId = mode.name().toLowerCase(java.util.Locale.ROOT);

                // ---------- RAW inputs -> RAW output ----------
                {
                    var b = ShapelessRecipeJsonBuilder
                            .create(RecipeCategory.MISC, RoseumItems.RAW_ROSEGOLD, count)
                            .criterion("has_copper_raw", conditionsFromTag(RoseumItemTagProvider.ALLOY_COPPER_RAW))
                            .criterion("has_gold_raw",   conditionsFromTag(RoseumItemTagProvider.ALLOY_GOLD_RAW));

                    for (int i = 0; i < copper; i++) b.input(RoseumItemTagProvider.ALLOY_COPPER_RAW);
                    for (int i = 0; i < gold;   i++) b.input(RoseumItemTagProvider.ALLOY_GOLD_RAW);

                    var condExporter = withConditions(
                            exporter,
                            // mode
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_mode"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", mode.name()); }
                            },
                            // count
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_output_count"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", count); }
                            },
                            // enabled?
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_enable_raw"); }
                                @Override public void writeParameters(JsonObject obj) { }
                            }
                    );

                    String idStr = "rosegold_alloy_" + modeId + "_raw_" + count + "_rawinputs";
                    b.offerTo(condExporter, Roseum.id(idStr));
                }

                // ---------- INGOT inputs -> INGOT output ----------
                {
                    var b = ShapelessRecipeJsonBuilder
                            .create(RecipeCategory.MISC, RoseumItems.ROSEGOLD_INGOT, count)
                            .criterion("has_copper_ingot", conditionsFromTag(RoseumItemTagProvider.ALLOY_COPPER_INGOTS))
                            .criterion("has_gold_ingot",   conditionsFromTag(RoseumItemTagProvider.ALLOY_GOLD_INGOTS));

                    for (int i = 0; i < copper; i++) b.input(RoseumItemTagProvider.ALLOY_COPPER_INGOTS);
                    for (int i = 0; i < gold;   i++) b.input(RoseumItemTagProvider.ALLOY_GOLD_INGOTS);

                    var condExporter = withConditions(
                            exporter,
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_mode"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", mode.name()); }
                            },
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_output_count"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", count); }
                            },
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_enable_ingot"); }
                                @Override public void writeParameters(JsonObject obj) { }
                            }
                    );

                    String idStr = "rosegold_alloy_" + modeId + "_ingot_" + count + "_ingotinputs";
                    b.offerTo(condExporter, Roseum.id(idStr));
                }

                // ---------- (optional) INGOT inputs -> RAW output ----------
                {
                    var b = ShapelessRecipeJsonBuilder
                            .create(RecipeCategory.MISC, RoseumItems.RAW_ROSEGOLD, count)
                            .criterion("has_copper_ingot", conditionsFromTag(RoseumItemTagProvider.ALLOY_COPPER_INGOTS))
                            .criterion("has_gold_ingot",   conditionsFromTag(RoseumItemTagProvider.ALLOY_GOLD_INGOTS));

                    for (int i = 0; i < copper; i++) b.input(RoseumItemTagProvider.ALLOY_COPPER_INGOTS);
                    for (int i = 0; i < gold;   i++) b.input(RoseumItemTagProvider.ALLOY_GOLD_INGOTS);

                    var condExporter = withConditions(
                            exporter,
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_mode"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", mode.name()); }
                            },
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_output_count"); }
                                @Override public void writeParameters(JsonObject obj) { obj.addProperty("value", count); }
                            },
                            new ConditionJsonProvider() {
                                @Override public Identifier getConditionId() { return Roseum.id("alloy_enable_ingot_to_raw"); }
                                @Override public void writeParameters(JsonObject obj) { }
                            }
                    );

                    String idStr = "rosegold_alloy_" + modeId + "_raw_" + count + "_ingotinputs";
                    b.offerTo(condExporter, Roseum.id(idStr));
                }
            }
        }

        
        
        
    }
}
