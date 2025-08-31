package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class RoseumItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> ALLOY_COPPER_INGOTS = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_copper_ingots"));
    public static final TagKey<Item> ALLOY_COPPER_RAW    = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_copper_raw"));
    public static final TagKey<Item> ALLOY_COPPER_BOTH   = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_copper_both"));

    public static final TagKey<Item> ALLOY_GOLD_INGOTS   = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_gold_ingots"));
    public static final TagKey<Item> ALLOY_GOLD_RAW      = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_gold_raw"));
    public static final TagKey<Item> ALLOY_GOLD_BOTH     = TagKey.of(RegistryKeys.ITEM, Roseum.id("alloy_gold_both"));

    public RoseumItemTagProvider(FabricDataOutput out, CompletableFuture<RegistryWrapper.WrapperLookup> regs) {
        super(out, regs);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(ALLOY_COPPER_INGOTS).add(Items.COPPER_INGOT);
        getOrCreateTagBuilder(ALLOY_COPPER_RAW).add(Items.RAW_COPPER);
        getOrCreateTagBuilder(ALLOY_COPPER_BOTH).addTag(ALLOY_COPPER_INGOTS).addTag(ALLOY_COPPER_RAW);

        getOrCreateTagBuilder(ALLOY_GOLD_INGOTS).add(Items.GOLD_INGOT);
        getOrCreateTagBuilder(ALLOY_GOLD_RAW).add(Items.RAW_GOLD);
        getOrCreateTagBuilder(ALLOY_GOLD_BOTH).addTag(ALLOY_GOLD_INGOTS).addTag(ALLOY_GOLD_RAW);
    }
}
