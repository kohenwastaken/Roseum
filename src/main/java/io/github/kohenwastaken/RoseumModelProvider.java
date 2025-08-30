package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class RoseumModelProvider extends FabricModelProvider {
    public RoseumModelProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(RoseumBlocks.ROSEGOLD_BLOCK);
        gen.registerSimpleCubeAll(RoseumBlocks.RAW_ROSEGOLD_BLOCK);
        gen.registerDoor(RoseumBlocks.ROSEGOLD_DOOR);
        gen.registerTrapdoor(RoseumBlocks.ROSEGOLD_TRAPDOOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(RoseumItems.ROSEGOLD_INGOT, Models.GENERATED);
        gen.register(RoseumItems.ROSEGOLD_NUGGET, Models.GENERATED);
        gen.register(RoseumItems.RAW_ROSEGOLD, Models.GENERATED);
        gen.register(RoseumItems.ROSEGOLD_SWORD,   net.minecraft.data.client.Models.HANDHELD);
        gen.register(RoseumItems.ROSEGOLD_PICKAXE, net.minecraft.data.client.Models.HANDHELD);
        gen.register(RoseumItems.ROSEGOLD_AXE,     net.minecraft.data.client.Models.HANDHELD);
        gen.register(RoseumItems.ROSEGOLD_SHOVEL,  net.minecraft.data.client.Models.HANDHELD);
        gen.register(RoseumItems.ROSEGOLD_HOE,     net.minecraft.data.client.Models.HANDHELD);
        gen.register(RoseumItems.ROSEGOLD_HELMET,     net.minecraft.data.client.Models.GENERATED);
        gen.register(RoseumItems.ROSEGOLD_CHESTPLATE, net.minecraft.data.client.Models.GENERATED);
        gen.register(RoseumItems.ROSEGOLD_LEGGINGS,   net.minecraft.data.client.Models.GENERATED);
        gen.register(RoseumItems.ROSEGOLD_BOOTS,      net.minecraft.data.client.Models.GENERATED);


    }
}
