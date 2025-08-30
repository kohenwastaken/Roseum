package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class RoseumBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public RoseumBlockTagProvider(FabricDataOutput output,
                                  CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                RoseumBlocks.ROSEGOLD_BLOCK,
                RoseumBlocks.RAW_ROSEGOLD_BLOCK,
                RoseumBlocks.ROSEGOLD_DOOR,
                RoseumBlocks.ROSEGOLD_TRAPDOOR
        );

        
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(
                RoseumBlocks.ROSEGOLD_BLOCK,
                RoseumBlocks.RAW_ROSEGOLD_BLOCK,
                RoseumBlocks.ROSEGOLD_DOOR,
                RoseumBlocks.ROSEGOLD_TRAPDOOR
                
        );
        
    }
}
