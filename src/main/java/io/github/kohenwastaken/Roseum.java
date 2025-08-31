package io.github.kohenwastaken;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

public class Roseum implements ModInitializer {
    public static final String MOD_ID = "roseum";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        RoseumConfig.load();
        RoseumResourceConditions.register();

        RoseumItems.registerModItems();
        RoseumBlocks.registerModBlocks();
        RoseumItemGroup.register();

        LOGGER.info("Roseum init");
    }

    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }
}
