
package io.github.cruciblemc.necrotempus.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class SyncGlyphConfig implements IMessage {
    private JsonObject config;

    public SyncGlyphConfig() {}

    public SyncGlyphConfig(JsonObject config) {
        this.config = config;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String jsonString = ByteBufUtils.readUTF8String(buf);
        this.config = new JsonParser().parse(jsonString).getAsJsonObject();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, config.toString());
    }

    public static class Handler implements IMessageHandler<SyncGlyphConfig, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncGlyphConfig message, MessageContext ctx) {
            // 1.7.10 主线程调度
            Minecraft.getMinecraft().func_152344_a(() -> {
                GlyphManager.handleConfigUpdate(message.config);
            });
            return null;
        }
    }
}
