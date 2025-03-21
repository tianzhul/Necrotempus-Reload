// GlyphManager.java
package io.github.cruciblemc.necrotempus.packet;

import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GlyphManager {
    private static final Logger LOGGER = LogManager.getLogger("NecroTempus|Glyph");
    private static final Map<Character, GlyphData> GLYPH_MAP = new HashMap<>();

    public static void loadDefaultConfig() {
        // 加载默认配置或空配置
        GLYPH_MAP.clear();
    }
    public static void handleConfigUpdate(JsonObject config) {
        GLYPH_MAP.clear();
        loadConfig(config);
    }

    private static final int CONFIG_VERSION = 1;
    public int getProtocolVersion() {
        return CONFIG_VERSION;
    }
    public static void loadConfig(JsonObject config) {
        GLYPH_MAP.clear();
        for (Map.Entry<String, com.google.gson.JsonElement> entry : config.entrySet()) {
            char key = entry.getKey().charAt(0);
            JsonObject data = entry.getValue().getAsJsonObject();

            String texture = data.get("texture").getAsString();
            int width = data.get("width").getAsInt();
            int height = data.get("height").getAsInt();

            GLYPH_MAP.put(key, new GlyphData(texture, width, height));
            loadTextureAsync(texture);
        }
    }

    private static void loadTextureAsync(String texture) {
        new Thread(() -> {
            try {
                String[] parts = texture.split("]", 2);
                String type = parts[0].substring(1);
                String path = parts[1].replace("\"", "").trim();

                TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
                ResourceLocation resLoc = new ResourceLocation(path);

                if (type.equals("local")) {
                    textureManager.bindTexture(resLoc);
                } else if (type.equals("url")) {
                    BufferedImage image = ImageIO.read(new URL(path));
                    textureManager.loadTexture(resLoc, new DynamicTexture(image));
                }
            } catch (Exception e) {
                LOGGER.error("Texture load failed: " + texture, e);
            }
        }).start();
    }

    public static GlyphData getGlyph(char c) {
        return GLYPH_MAP.get(c);
    }

    public static class GlyphData {
        public final String texture;
        public final int width;
        public final int height;

        public GlyphData(String texture, int width, int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
    }
}