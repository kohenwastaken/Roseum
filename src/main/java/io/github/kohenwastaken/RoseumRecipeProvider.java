package io.github.kohenwastaken;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class RoseumRecipeProvider extends FabricRecipeProvider {
    public RoseumRecipeProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // Reçeteleri sonra ekleyeceğiz.
    }
}
