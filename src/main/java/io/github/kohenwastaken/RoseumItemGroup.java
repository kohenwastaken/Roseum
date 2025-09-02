package io.github.kohenwastaken;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class RoseumItemGroup {
    private RoseumItemGroup() {}

    public static void register() {
        ItemGroup group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(RoseumItems.ROSEGOLD_INGOT))
                .displayName(Text.translatable("itemGroup.roseum.general"))
                .entries((ctx, entries) -> {
                	
                    entries.add(RoseumItems.ROSEGOLD_INGOT);
                    entries.add(RoseumItems.ROSEGOLD_NUGGET);
                    entries.add(RoseumItems.RAW_ROSEGOLD);
                    
                    entries.add(RoseumBlocks.ROSEGOLD_BLOCK);
                    entries.add(RoseumBlocks.RAW_ROSEGOLD_BLOCK);
                    entries.add(RoseumBlocks.ROSEGOLD_DOOR);
                    entries.add(RoseumBlocks.ROSEGOLD_TRAPDOOR);
                    
                    entries.add(RoseumItems.ROSEGOLD_SWORD);
                    entries.add(RoseumItems.ROSEGOLD_PICKAXE);
                    entries.add(RoseumItems.ROSEGOLD_AXE);
                    entries.add(RoseumItems.ROSEGOLD_SHOVEL);
                    entries.add(RoseumItems.ROSEGOLD_HOE);
                    
                    entries.add(RoseumItems.ROSEGOLD_HELMET);
                    entries.add(RoseumItems.ROSEGOLD_CHESTPLATE);
                    entries.add(RoseumItems.ROSEGOLD_LEGGINGS);
                    entries.add(RoseumItems.ROSEGOLD_BOOTS);
                    
                    entries.add(RoseumItems.ALLOY_TEMPLATE);
                })
                .build();

        Registry.register(Registries.ITEM_GROUP, new Identifier(Roseum.MOD_ID, "general"), group);
    }
}
