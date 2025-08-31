package io.github.kohenwastaken;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class RoseumConfig {
    // Player-facing options
    public enum Mode { C3_G1, C2_G2, C1_G3 }          // 3C+1G, 2C+2G, 1C+3G
    public enum InputKind { INGOT, RAW, BOTH }        // allowed inputs

    public Mode mode = Mode.C3_G1;
    public int outputCount = 1;                       // clamped to 1..4
    public InputKind inputKind = InputKind.BOTH;
    public boolean allowIngotToRaw = false;           // if true: also add INGOT -> RAW alloy

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "roseum.json";

    public static RoseumConfig INSTANCE = new RoseumConfig();

    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
            if (Files.exists(file)) {
                try (Reader r = Files.newBufferedReader(file)) {
                    JsonObject obj = JsonParser.parseReader(r).getAsJsonObject();

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
                    if (obj.has("allowIngotToRaw")) {
                        INSTANCE.allowIngotToRaw = obj.get("allowIngotToRaw").getAsBoolean();
                    }
                }
            }
        } catch (Exception e) {
            Roseum.LOGGER.error("Failed to load Roseum config", e);
        }

        // clamp and persist in a readable format
        INSTANCE.outputCount = Math.max(1, Math.min(4, INSTANCE.outputCount));
        savePretty();
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
            help.addProperty("allowIngotToRaw",
                    "When inputKind includes INGOT, also add an extra recipe that crafts RAW alloy from ingot inputs (default: false).");
            root.add("_help", help);

            root.addProperty("mode", INSTANCE.mode.name());
            root.addProperty("inputKind", INSTANCE.inputKind.name());
            root.addProperty("outputCount", INSTANCE.outputCount);
            root.addProperty("allowIngotToRaw", INSTANCE.allowIngotToRaw);

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
