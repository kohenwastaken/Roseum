package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class RoseumDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(RoseumModelProvider::new);
        pack.addProvider(RoseumLootTableProvider::new);
        pack.addProvider(RoseumRecipeProvider::new);
        pack.addProvider(RoseumBlockTagProvider::new);
    }
}
