package io.github.kohenwastaken;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class RoseumModMenu implements ModMenuApi {

    // Discrete 1/2/3/4 dropdown — DropdownMenuBuilder'a bağımlılık yok
    private enum Count {
        ONE(1), TWO(2), THREE(3), FOUR(4);
        final int v;
        Count(int v) { this.v = v; }
        static Count from(int x) {
            return switch (x) {
                case 1 -> ONE;
                case 2 -> TWO;
                case 3 -> THREE;
                default -> FOUR;
            };
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            final var cfg = RoseumConfig.INSTANCE;

            var builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Roseum Config"));

            builder.setSavingRunnable(() -> {
                RoseumConfig.savePretty();
                var mc = MinecraftClient.getInstance();
                if (mc.player != null) {
                    mc.player.sendMessage(Text.literal(
                            "[Roseum] Config saved. Use /reload or restart to apply."
                    ), false);
                }
            });

            var cat = builder.getOrCreateCategory(Text.literal("General"));
            var eb  = builder.entryBuilder();

            // --- Alloy Mode (insan-okur isimler) ---
            var modeSel = eb.startEnumSelector(
                            Text.literal("Alloy Mode"),
                            RoseumConfig.Mode.class,
                            cfg.mode)
                    .setEnumNameProvider(e -> {
                        RoseumConfig.Mode m = (RoseumConfig.Mode) e;   // <- Enum olarak geliyor, cast et
                        return switch (m) {
                            case C3_G1 -> Text.literal("3 Copper + 1 Gold");
                            case C2_G2 -> Text.literal("2 Copper + 2 Gold");
                            case C1_G3 -> Text.literal("1 Copper + 3 Gold");
                        };
                    })
                    .setDefaultValue(RoseumConfig.Mode.C3_G1)
                    .setSaveConsumer(v -> cfg.mode = v)
                    .setTooltip(Text.literal("How many copper + gold are required (total 4)."));
            cat.addEntry(modeSel.build());

            // --- Allowed Inputs ---
            var ingotToRawEntry = eb.startBooleanToggle(
                            Text.literal("Allow Ingot → Raw Alloy (extra recipe)"),
                            cfg.allowIngotToRaw)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("When inputs are ingots, also add a recipe that crafts RAW alloy."))
                    .setSaveConsumer(v -> cfg.allowIngotToRaw = v)
                    .build();

            var inputSel = eb.startEnumSelector(
                            Text.literal("Allowed Inputs"),
                            RoseumConfig.InputKind.class,
                            cfg.inputKind)
                    .setEnumNameProvider(e -> {
                        RoseumConfig.InputKind k = (RoseumConfig.InputKind) e;
                        return switch (k) {
                            case INGOT -> Text.literal("Ingots Only");
                            case RAW   -> Text.literal("Raw Ores Only");
                            case BOTH  -> Text.literal("Both (mix allowed)");
                        };
                    })
                    .setDefaultValue(RoseumConfig.InputKind.BOTH)
                    .setTooltip(Text.literal("Which item forms are accepted in crafting."))
                    .setSaveConsumer(v -> {
                        cfg.inputKind = v;
                        boolean enable = (v != RoseumConfig.InputKind.RAW);
                        ingotToRawEntry.setEditable(enable); // RAW ise kilitle
                        if (!enable) {
                            cfg.allowIngotToRaw = false;      // değeri de kapat
                        }
                    });
            cat.addEntry(inputSel.build());

            // Başlangıç kilit durumu
            ingotToRawEntry.setEditable(cfg.inputKind != RoseumConfig.InputKind.RAW);
            cat.addEntry(ingotToRawEntry);

            // --- Output Count: 1/2/3/4 dropdown ---
            var countSel = eb.startEnumSelector(
                            Text.literal("Output Count"),
                            Count.class,
                            Count.from(cfg.outputCount))
                    .setEnumNameProvider(e -> Text.literal(Integer.toString(((Count) e).v)))
                    .setDefaultValue(Count.ONE)
                    .setTooltip(Text.literal("How many alloy items the recipe crafts."))
                    .setSaveConsumer(c -> cfg.outputCount = ((Count) c).v);
            cat.addEntry(countSel.build());

            // bilgi
            cat.addEntry(eb.startTextDescription(Text.literal(
                    "Changes are saved immediately, but take effect after /reload or restarting the game."
            )).build());

            return builder.build();
        };
    }
}
