package io.github.kohenwastaken;

import io.github.kohenwastaken.recipe.RoseumSmithingAlloyRecipe;
import io.github.kohenwastaken.recipe.RoseumSmithingTransformRecipe;
import io.github.kohenwastaken.recipe.serializer.RoseumSmithingAlloySerializer;
import io.github.kohenwastaken.recipe.serializer.RoseumSmithingTransformSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class RoseumSmithing {
    public static RecipeSerializer<RoseumSmithingAlloyRecipe> ALLOY_SERIALIZER;
    public static RecipeSerializer<RoseumSmithingTransformRecipe> TRANSFORM_SERIALIZER;

    public static void init() {
        ALLOY_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                Roseum.id("smithing_alloy"),
                new RoseumSmithingAlloySerializer()
        );
        TRANSFORM_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                Roseum.id("smithing_transform"),
                new RoseumSmithingTransformSerializer()
        );
    }
}
