package io.github.kohenwastaken;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.util.Identifier;

public final class RoseumResourceConditions {
    private RoseumResourceConditions() {}

    public static void register() {
        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "alloy_mode"),
                json -> RoseumConfig.INSTANCE.mode.name().equalsIgnoreCase(json.getAsJsonObject().get("value").getAsString()));

        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "alloy_output_count"),
                json -> RoseumConfig.INSTANCE.outputCount == json.getAsJsonObject().get("value").getAsInt());

        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "alloy_enable_raw"),
                json -> {
                    var k = RoseumConfig.INSTANCE.inputKind;
                    return k == RoseumConfig.InputKind.RAW || k == RoseumConfig.InputKind.BOTH;
                });

        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "alloy_enable_ingot"),
                json -> {
                    var k = RoseumConfig.INSTANCE.inputKind;
                    return k == RoseumConfig.InputKind.INGOT || k == RoseumConfig.InputKind.BOTH;
                });
        
        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "crafting_alloy_enabled"),
                json -> RoseumConfig.INSTANCE.enableCraftingAlloy);

        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "smithing_alloy_enabled"),
                json -> RoseumConfig.INSTANCE.enableSmithingAlloy);

        ResourceConditions.register(new Identifier(Roseum.MOD_ID, "smithing_transform_enabled"),
                json -> RoseumConfig.INSTANCE.enableSmithingTransform);


        
    }
}
