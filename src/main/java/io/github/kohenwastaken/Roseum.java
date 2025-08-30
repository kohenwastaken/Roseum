package io.github.kohenwastaken;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Roseum implements ModInitializer {
    public static final String MOD_ID = "roseum";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
    	
    	RoseumItems.registerModItems();
    	RoseumBlocks.registerModBlocks();
    	RoseumItemGroup.register();

        LOGGER.info("Roseum init");
    }
}
