package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class RoseumLootTableProvider extends FabricBlockLootTableProvider {
    public RoseumLootTableProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generate() {
        addDrop(RoseumBlocks.ROSEGOLD_BLOCK);
        addDrop(RoseumBlocks.RAW_ROSEGOLD_BLOCK);
        addDrop(RoseumBlocks.ROSEGOLD_DOOR, doorDrops(RoseumBlocks.ROSEGOLD_DOOR));
        addDrop(RoseumBlocks.ROSEGOLD_TRAPDOOR);
    }
}
