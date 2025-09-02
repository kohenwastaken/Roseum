package io.github.kohenwastaken;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class RoseumConfig {

    // ===== Crafting – Alloy (intended alanlar aynen kalsın) =====
    public enum Mode { C3_G1, C2_G2, C1_G3 }          // 3C+1G, 2C+2G, 1C+3G
    public enum InputKind { INGOT, RAW, BOTH }        // allowed inputs

    public Mode mode = Mode.C3_G1;
    public int outputCount = 1;                       // clamped to 1..4 (INTENDED)
    public InputKind inputKind = InputKind.BOTH;

    // Sadece alloy crafting’i komple aç/kapa
    public boolean enableCraftingAlloy = false;

    // ===== Smithing – Tek politika enumu =====
    public enum TemplatePolicy { OFF, DO_NOT_CONSUME, CONSUME, DAMAGE }

    public boolean enableSmithingAlloy = true;
    public boolean enableSmithingTransform = true;

    public TemplatePolicy smithingAlloy_templatePolicy = TemplatePolicy.OFF;
    public TemplatePolicy smithingTransform_templatePolicy = TemplatePolicy.OFF;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "roseum.json";

    public static final RoseumConfig INSTANCE = new RoseumConfig();
    private RoseumConfig() {}

    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
            if (Files.exists(file)) {
                try (Reader r = Files.newBufferedReader(file)) {
                    JsonObject obj = JsonParser.parseReader(r).getAsJsonObject();

                    // ---- crafting (INTENDED alanlar) ----
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

                    // ---- yeni toggle’lar + politika ----
                    if (obj.has("crafting") && obj.get("crafting").isJsonObject()) {
                        JsonObject c = obj.getAsJsonObject("crafting");
                        if (c.has("alloy") && c.get("alloy").isJsonObject()) {
                            JsonObject a = c.getAsJsonObject("alloy");
                            if (a.has("enabled")) INSTANCE.enableCraftingAlloy = a.get("enabled").getAsBoolean();
                        }
                    }

                    if (obj.has("smithing") && obj.get("smithing").isJsonObject()) {
                        JsonObject s = obj.getAsJsonObject("smithing");

                        if (s.has("alloy") && s.get("alloy").isJsonObject()) {
                            JsonObject a = s.getAsJsonObject("alloy");
                            if (a.has("enabled")) INSTANCE.enableSmithingAlloy = a.get("enabled").getAsBoolean();
                            if (a.has("templatePolicy")) {
                                INSTANCE.smithingAlloy_templatePolicy = parsePolicy(a.get("templatePolicy").getAsString(), TemplatePolicy.OFF);
                            }
                        }

                        if (s.has("transform") && s.get("transform").isJsonObject()) {
                            JsonObject t = s.getAsJsonObject("transform");
                            if (t.has("enabled")) INSTANCE.enableSmithingTransform = t.get("enabled").getAsBoolean();
                            if (t.has("templatePolicy")) {
                                INSTANCE.smithingTransform_templatePolicy = parsePolicy(t.get("templatePolicy").getAsString(), TemplatePolicy.CONSUME);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Roseum.LOGGER.error("Failed to load Roseum config", e);
        }

        // sanitize
        INSTANCE.outputCount = Math.max(1, Math.min(4, INSTANCE.outputCount));

        // pretty persist
        savePretty();
    }

    private static TemplatePolicy parsePolicy(String s, TemplatePolicy def) {
        if (s == null) return def;
        try { return TemplatePolicy.valueOf(s.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ex) { return def; }
    }

    public static void savePretty() {
        try {
            Path dir = FabricLoader.getInstance().getConfigDir();
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);

            JsonObject root = new JsonObject();
            root.addProperty("_comment",
                    "Roseum configuration. Edit values, then use /reload (singleplayer: re-enter world) or restart the game. The _help fields are informational only.");

            // ---- _help ----
            JsonObject help = new JsonObject();
            help.add("mode_options", arr("C3_G1", "C2_G2", "C1_G3"));
            help.add("inputKind_options", arr("INGOT", "RAW", "BOTH"));
            help.add("outputCount_range", arr(1, 2, 3, 4));
            help.add("templatePolicy_options", arr("OFF", "DO_NOT_CONSUME", "CONSUME", "DAMAGE"));
            root.add("_help", help);

            // ---- crafting (INTENDED) ----
            root.addProperty("mode", INSTANCE.mode.name());
            root.addProperty("inputKind", INSTANCE.inputKind.name());
            root.addProperty("outputCount", INSTANCE.outputCount);

            // ---- sadece alloy crafting toggle’ı ----
            JsonObject crafting = new JsonObject();
            JsonObject craftingAlloy = new JsonObject();
            craftingAlloy.addProperty("enabled", INSTANCE.enableCraftingAlloy);
            crafting.add("alloy", craftingAlloy);
            root.add("crafting", crafting);

            // ---- smithing ----
            JsonObject smithing = new JsonObject();

            JsonObject alloy = new JsonObject();
            alloy.addProperty("enabled", INSTANCE.enableSmithingAlloy);
            alloy.addProperty("templatePolicy", INSTANCE.smithingAlloy_templatePolicy.name());
            smithing.add("alloy", alloy);

            JsonObject transform = new JsonObject();
            transform.addProperty("enabled", INSTANCE.enableSmithingTransform);
            transform.addProperty("templatePolicy", INSTANCE.smithingTransform_templatePolicy.name());
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
