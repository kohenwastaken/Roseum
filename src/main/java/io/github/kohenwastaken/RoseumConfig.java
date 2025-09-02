package io.github.kohenwastaken;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class RoseumConfig {
    // ===== Player-facing (crafting tarafı) =====
    public enum Mode { C3_G1, C2_G2, C1_G3 }          // 3C+1G, 2C+2G, 1C+3G
    public enum InputKind { INGOT, RAW, BOTH }        // allowed inputs

    public Mode mode = Mode.C3_G1;
    public int outputCount = 1;                       // crafting için, 1..4 clamp
    public InputKind inputKind = InputKind.BOTH;

    // ===== Smithing davranışları (tek çıktı) =====
    // alloy
    public boolean smithingAlloy_requireTemplate = false;
    public String  smithingAlloy_templateBehavior = "consume";  // consume | return | damage
    // transform
    public boolean smithingTransform_requireTemplate = true;
    public String  smithingTransform_templateBehavior = "consume"; // consume | return | damage

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "roseum.json";

    public static RoseumConfig INSTANCE = new RoseumConfig();

    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
            if (Files.exists(file)) {
                try (Reader r = Files.newBufferedReader(file)) {
                    JsonObject obj = JsonParser.parseReader(r).getAsJsonObject();

                    // ---- crafting (mevcut alanlar) ----
                    if (obj.has("mode")) {
                        String s = obj.get("mode").getAsString().toUpperCase(Locale.ROOT);
                        try { INSTANCE.mode = Mode.valueOf(s); } catch (IllegalArgumentException ignored) {}
                    }
                    if (obj.has("inputKind")) {
                        String s = obj.get("inputKind").getAsString().toUpperCase(Locale.ROOT);
                        try { INSTANCE.inputKind = InputKind.valueOf(s); } catch (IllegalArgumentException ignored) {}
                    }
                    if (obj.has("outputCount")) {
                        INSTANCE.outputCount = obj.get("outputCount").getAsInt();
                    }

                    // ---- smithing (yeni alanlar) ----
                    if (obj.has("smithing") && obj.get("smithing").isJsonObject()) {
                        JsonObject s = obj.getAsJsonObject("smithing");

                        if (s.has("alloy") && s.get("alloy").isJsonObject()) {
                            JsonObject a = s.getAsJsonObject("alloy");
                            if (a.has("requireTemplate")) {
                                INSTANCE.smithingAlloy_requireTemplate = a.get("requireTemplate").getAsBoolean();
                            }
                            if (a.has("templateBehavior")) {
                                INSTANCE.smithingAlloy_templateBehavior = a.get("templateBehavior").getAsString();
                            }
                        }

                        if (s.has("transform") && s.get("transform").isJsonObject()) {
                            JsonObject t = s.getAsJsonObject("transform");
                            if (t.has("requireTemplate")) {
                                INSTANCE.smithingTransform_requireTemplate = t.get("requireTemplate").getAsBoolean();
                            }
                            if (t.has("templateBehavior")) {
                                INSTANCE.smithingTransform_templateBehavior = t.get("templateBehavior").getAsString();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Roseum.LOGGER.error("Failed to load Roseum config", e);
        }

        // ---- sanitize ----
        INSTANCE.outputCount = Math.max(1, Math.min(4, INSTANCE.outputCount));
        INSTANCE.smithingAlloy_templateBehavior      = sanitizeBehavior(INSTANCE.smithingAlloy_templateBehavior);
        INSTANCE.smithingTransform_templateBehavior  = sanitizeBehavior(INSTANCE.smithingTransform_templateBehavior);

        savePretty();
    }

    private static String sanitizeBehavior(String raw) {
        if (raw == null) return "consume";
        String v = raw.toLowerCase(Locale.ROOT).trim();
        return (v.equals("consume") || v.equals("return") || v.equals("damage")) ? v : "consume";
    }

    public static void savePretty() {
        try {
            Path dir = FabricLoader.getInstance().getConfigDir();
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);

            JsonObject root = new JsonObject();

            root.addProperty("_comment",
                    "Roseum configuration. Edit values, then use /reload (singleplayer: re-enter world) "
                  + "or restart the game. The _help fields are informational only.");

            // ---- _help ----
            JsonObject help = new JsonObject();
            help.add("mode_options", arr(
                    "C3_G1 (3 copper + 1 gold)",
                    "C2_G2 (2 copper + 2 gold)",
                    "C1_G3 (1 copper + 3 gold)"
            ));
            help.add("inputKind_options", arr(
                    "INGOT (only ingots)",
                    "RAW (only raw ores)",
                    "BOTH (any mix of ingots and raw ores)"
            ));
            help.add("outputCount_range", arr(1, 2, 3, 4));

            JsonObject smithHelp = new JsonObject();
            smithHelp.add("templateBehavior_options", arr("consume", "return", "damage"));
            smithHelp.addProperty("note",
                    "Smithing has single output. 'return' puts the template back; 'damage' reduces durability or acts like 'return' if not damageable.");
            help.add("smithing", smithHelp);

            root.add("_help", help);

            // ---- crafting alanları ----
            root.addProperty("mode", INSTANCE.mode.name());
            root.addProperty("inputKind", INSTANCE.inputKind.name());
            root.addProperty("outputCount", INSTANCE.outputCount);

            // ---- smithing alanları ----
            JsonObject smithing = new JsonObject();

            JsonObject alloy = new JsonObject();
            alloy.addProperty("requireTemplate", INSTANCE.smithingAlloy_requireTemplate);
            alloy.addProperty("templateBehavior", INSTANCE.smithingAlloy_templateBehavior);

            JsonObject transform = new JsonObject();
            transform.addProperty("requireTemplate", INSTANCE.smithingTransform_requireTemplate);
            transform.addProperty("templateBehavior", INSTANCE.smithingTransform_templateBehavior);

            smithing.add("alloy", alloy);
            smithing.add("transform", transform);

            root.add("smithing", smithing);

            try (Writer w = Files.newBufferedWriter(file)) {
                GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Roseum.LOGGER.error("Failed to save Roseum config", e);
        }
    }

    private static JsonArray arr(Object... vals) {
        JsonArray a = new JsonArray();
        for (Object v : vals) {
            if (v instanceof Number n) a.add(n);
            else a.add(String.valueOf(v));
        }
        return a;
    }
}
