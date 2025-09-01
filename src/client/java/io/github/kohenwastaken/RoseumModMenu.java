package io.github.kohenwastaken;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class RoseumModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // YACL yüklü mü?
        	
        	boolean hasYacl = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")
                    || FabricLoader.getInstance().isModLoaded("yacl");
        	
        	if (!hasYacl) {
                // Basit bir bilgi ekranı göster
                return new InfoScreen(parent, Text.literal(
                        "Roseum: This config screen requires YetAnotherConfigLib.\n" +
                        "Please install YACL to edit settings in-game."));
            }
            try {
                // YACL ekranını ayrı sınıftan reflection ile çağır
                Class<?> factory = Class.forName("io.github.kohenwastaken.RoseumYaclScreenFactory");
                return (Screen) factory.getMethod("create", Screen.class).invoke(null, parent);
            } catch (Throwable t) {
                Roseum.LOGGER.error("Failed to open YACL config screen", t);
                return new InfoScreen(parent, Text.literal("Failed to open config screen. See logs."));
            }
        };
    }

    // Küçük “bilgi” ekranı (YACL yoksa)
    static class InfoScreen extends Screen {
        private final Screen parent;
        protected InfoScreen(Screen parent, Text title) {
            super(title);
            this.parent = parent;
        }
        @Override
        protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.literal("Done"),
                    b -> this.client.setScreen(parent))
                .dimensions(this.width / 2 - 50, this.height / 2 + 20, 100, 20)
                .build());
        }
        @Override
        public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
            renderBackground(ctx);
            ctx.drawCenteredTextWithShadow(textRenderer, getTitle(), width/2, height/2 - 10, 0xFFFFFF);
            super.render(ctx, mouseX, mouseY, delta);
        }
    }
}
