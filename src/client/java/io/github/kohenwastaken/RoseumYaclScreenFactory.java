package io.github.kohenwastaken;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public final class RoseumYaclScreenFactory {

    private RoseumYaclScreenFactory() {}

    public static Screen create(Screen parent) {
        final var cfg = RoseumConfig.INSTANCE;

        // --- Alloy Mode (insan-okur etiketler) ---
        Option<RoseumConfig.Mode> modeOpt = Option.<RoseumConfig.Mode>createBuilder()
                .name(Text.literal("Alloy Mode"))
                .description(OptionDescription.of(Text.literal("How many copper + gold are required (total 4).")))
                .binding(cfg.mode, () -> cfg.mode, v -> cfg.mode = v)
                .controller(opt -> EnumControllerBuilder.create(opt)
                        .enumClass(RoseumConfig.Mode.class)
                        .formatValue(m -> switch (m) {
                            case C3_G1 -> Text.literal("3 Copper + 1 Gold");
                            case C2_G2 -> Text.literal("2 Copper + 2 Gold");
                            case C1_G3 -> Text.literal("1 Copper + 3 Gold");
                        }))
                .build();

        // --- Allowed Inputs (INGOT / RAW / BOTH) ---
        Option<RoseumConfig.InputKind> inputOpt = Option.<RoseumConfig.InputKind>createBuilder()
                .name(Text.literal("Allowed Inputs"))
                .description(OptionDescription.of(Text.literal("Which item forms are accepted in crafting.")))
                .binding(cfg.inputKind, () -> cfg.inputKind, v -> cfg.inputKind = v)
                .controller(opt -> EnumControllerBuilder.create(opt)
                        .enumClass(RoseumConfig.InputKind.class)
                        .formatValue(k -> switch (k) {
                            case INGOT -> Text.literal("Ingots Only");
                            case RAW   -> Text.literal("Raw Ores Only");
                            case BOTH  -> Text.literal("Both (mix allowed)");
                        }))
                .build();

        // --- Output Count: 1..4 slider, adım = 1 ---
        Option<Integer> countOpt = Option.<Integer>createBuilder()
                .name(Text.literal("Output Count"))
                .description(OptionDescription.of(Text.literal("How many alloy items the recipe crafts.")))
                .binding(cfg.outputCount, () -> cfg.outputCount, v -> cfg.outputCount = v)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(1, 4)
                        .step(1)
                        .formatValue(i -> Text.literal(Integer.toString(i))))
                .build();

        OptionGroup general = OptionGroup.createBuilder()
                .name(Text.literal("General"))
                .option(modeOpt)
                .option(inputOpt)
                .option(countOpt)
                .build();

        ConfigCategory cat = ConfigCategory.createBuilder()
                .name(Text.literal("General"))
                .group(general)
                .build();

        Runnable saver = () -> {
            
            RoseumConfig.savePretty();

            var mc = MinecraftClient.getInstance();
            if (mc.player != null) {
                mc.player.sendMessage(
                        Text.literal("[Roseum] Config saved. Use /reload or restart to apply."), false);
            }
        };

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Roseum Config"))
                .category(cat)
                .save(saver)
                .build()
                .generateScreen(parent);
    }
}
