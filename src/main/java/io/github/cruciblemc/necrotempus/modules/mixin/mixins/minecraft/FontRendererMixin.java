// FontRendererMixin.java
package io.github.cruciblemc.necrotempus.modules.mixin.mixins.minecraft; // 必须与目标类同包

import io.github.cruciblemc.necrotempus.packet.GlyphManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Shadow
    protected abstract float renderUnicodeChar(char ch, boolean italic);

    @Inject(
            method = "renderUnicodeChar",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onRenderUnicodeChar(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        GlyphManager.GlyphData glyph = GlyphManager.getGlyph(ch);
        if (glyph != null) {
            renderCustomTexture(glyph);
            cir.setReturnValue((float) glyph.width);
            cir.cancel();
        }
    }

    @Unique
    private void renderCustomTexture(GlyphManager.GlyphData glyph) {
        Minecraft.getMinecraft().renderEngine.bindTexture(
                new ResourceLocation(glyph.texture.split("]", 2)[1])
        );
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0, glyph.height, 0, 0, 1);
        tess.addVertexWithUV(glyph.width, glyph.height, 0, 1, 1);
        tess.addVertexWithUV(glyph.width, 0, 0, 1, 0);
        tess.addVertexWithUV(0, 0, 0, 0, 0);
        tess.draw();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}