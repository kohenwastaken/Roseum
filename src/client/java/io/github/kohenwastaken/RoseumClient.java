package io.github.kohenwastaken;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class RoseumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderLayer.getCutout(),
                RoseumBlocks.ROSEGOLD_DOOR,
                RoseumBlocks.ROSEGOLD_TRAPDOOR
        );
    }
}
