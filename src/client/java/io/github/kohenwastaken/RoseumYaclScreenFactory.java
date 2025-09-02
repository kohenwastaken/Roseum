package io.github.kohenwastaken;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public final class RoseumYaclScreenFactory {

    private RoseumYaclScreenFactory() {}

    // UI için küçük bir enum; config’te string olarak saklanacak
    private enum TemplateBehavior { CONSUME, RETURN, DAMAGE }

    private static TemplateBehavior parseBehavior(String s) {
        if (s == null) return TemplateBehavior.CONSUME;
        switch (s.toLowerCase()) {
            case "return": return TemplateBehavior.RETURN;
            case "damage": return TemplateBehavior.DAMAGE;
            default:       return TemplateBehavior.CONSUME;
        }
    }

    private static String behaviorToString(TemplateBehavior b) {
        switch (b) {
            case RETURN: return "return";
            case DAMAGE: return "damage";
            default:     return "consume";
        }
    }

    public static Screen create(Screen parent) {
        final var cfg = RoseumConfig.INSTANCE;

        // ===== CRAFTING (mevcut) =====
        Option<RoseumConfig.Mode> modeOpt = Option.<RoseumConfig.Mode>createBuilder()
                .name(Text.literal("Alloy Mode (Crafting)"))
                .description(OptionDescription.of(Text.literal("How many copper + gold are required (total 4). Used by crafting recipes.")))
                .binding(cfg.mode, () -> cfg.mode, v -> cfg.mode = v)
                .controller(opt -> EnumControllerBuilder.create(opt)
                        .enumClass(RoseumConfig.Mode.class)
                        .formatValue(m -> switch (m) {
                            case C3_G1 -> Text.literal("3 Copper + 1 Gold");
                            case C2_G2 -> Text.literal("2 Copper + 2 Gold");
                            case C1_G3 -> Text.literal("1 Copper + 3 Gold");
                        }))
                .build();

        Option<RoseumConfig.InputKind> inputOpt = Option.<RoseumConfig.InputKind>createBuilder()
                .name(Text.literal("Allowed Inputs (Crafting)"))
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

        Option<Integer> countOpt = Option.<Integer>createBuilder()
                .name(Text.literal("Output Count (Crafting)"))
                .description(OptionDescription.of(Text.literal("How many alloy items the crafting recipe outputs.")))
                .binding(cfg.outputCount, () -> cfg.outputCount, v -> cfg.outputCount = v)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(1, 4)
                        .step(1)
                        .formatValue(i -> Text.literal(Integer.toString(i))))
                .build();

        OptionGroup craftingGroup = OptionGroup.createBuilder()
                .name(Text.literal("Crafting"))
                .option(modeOpt)
                .option(inputOpt)
                .option(countOpt)
                .build();

        ConfigCategory craftingCat = ConfigCategory.createBuilder()
                .name(Text.literal("Crafting"))
                .group(craftingGroup)
                .build();

        // ===== SMITHING – ALLOY =====
        Option<Boolean> alloyRequireTemplateOpt = Option.<Boolean>createBuilder()
                .name(Text.literal("Alloy: Require Template"))
                .description(OptionDescription.of(Text.literal("If enabled, the smithing alloy recipe needs a template in the left slot.")))
                .binding(cfg.smithingAlloy_requireTemplate,
                        () -> cfg.smithingAlloy_requireTemplate,
                        v  -> cfg.smithingAlloy_requireTemplate = v)
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<TemplateBehavior> alloyBehaviorOpt = Option.<TemplateBehavior>createBuilder()
                .name(Text.literal("Alloy: Template Behavior"))
                .description(OptionDescription.of(Text.literal("What happens to the template after smithing.\n- consume: vanilla behavior\n- return: give back\n- damage: reduce durability")))
                .binding(
                        parseBehavior(cfg.smithingAlloy_templateBehavior),
                        () -> parseBehavior(cfg.smithingAlloy_templateBehavior),
                        v  -> cfg.smithingAlloy_templateBehavior = behaviorToString(v)
                )
                .controller(opt -> EnumControllerBuilder.create(opt)
                        .enumClass(TemplateBehavior.class)
                        .formatValue(b -> switch (b) {
                            case RETURN -> Text.literal("Return (not consumed)");
                            case DAMAGE -> Text.literal("Damage (if damageable)");
                            default     -> Text.literal("Consume (vanilla)");
                        }))
                .build();

        OptionGroup smithAlloyGroup = OptionGroup.createBuilder()
                .name(Text.literal("Smithing – Alloy (1 Copper + 1 Gold → 1 Rose Gold Ingot)"))
                .option(alloyRequireTemplateOpt)
                .option(alloyBehaviorOpt)
                .build();

        // ===== SMITHING – TRANSFORM =====
        Option<Boolean> transRequireTemplateOpt = Option.<Boolean>createBuilder()
                .name(Text.literal("Transform: Require Template"))
                .description(OptionDescription.of(Text.literal("If enabled, transform recipes require a template.")))
                .binding(cfg.smithingTransform_requireTemplate,
                        () -> cfg.smithingTransform_requireTemplate,
                        v  -> cfg.smithingTransform_requireTemplate = v)
                .controller(BooleanControllerBuilder::create)
                .build();

        Option<TemplateBehavior> transBehaviorOpt = Option.<TemplateBehavior>createBuilder()
                .name(Text.literal("Transform: Template Behavior"))
                .description(OptionDescription.of(Text.literal("What happens to the template after smithing (transform recipes).")))
                .binding(
                        parseBehavior(cfg.smithingTransform_templateBehavior),
                        () -> parseBehavior(cfg.smithingTransform_templateBehavior),
                        v  -> cfg.smithingTransform_templateBehavior = behaviorToString(v)
                )
                .controller(opt -> EnumControllerBuilder.create(opt)
                        .enumClass(TemplateBehavior.class)
                        .formatValue(b -> switch (b) {
                            case RETURN -> Text.literal("Return (not consumed)");
                            case DAMAGE -> Text.literal("Damage (if damageable)");
                            default     -> Text.literal("Consume (vanilla)");
                        }))
                .build();

        OptionGroup smithTransformGroup = OptionGroup.createBuilder()
                .name(Text.literal("Smithing – Transform (Gold Armor + Copper → Rose Gold Armor)"))
                .option(transRequireTemplateOpt)
                .option(transBehaviorOpt)
                .build();

        ConfigCategory smithingCat = ConfigCategory.createBuilder()
                .name(Text.literal("Smithing"))
                .group(smithAlloyGroup)
                .group(smithTransformGroup)
                .build();

        // ===== Save callback =====
        Runnable saver = () -> {
            // Güvence için clamp
            cfg.outputCount = Math.max(1, Math.min(4, cfg.outputCount));
            RoseumConfig.savePretty();

            var mc = MinecraftClient.getInstance();
            if (mc.player != null) {
                mc.player.sendMessage(
                        Text.literal("[Roseum] Config saved. Use /reload (or re-enter world) to apply recipe changes."),
                        false
                );
            }
        };

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Roseum Config"))
                .category(craftingCat)
                .category(smithingCat)
                .save(saver)
                .build()
                .generateScreen(parent);
    }
}
