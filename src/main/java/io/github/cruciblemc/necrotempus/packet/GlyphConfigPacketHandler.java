package io.github.cruciblemc.necrotempus.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.cruciblemc.necrotempus.NecroTempus;
import net.minecraft.client.Minecraft;

public class GlyphConfigPacketHandler implements IMessageHandler<GlyphConfigPacket, IMessage> {
    @Override
    public IMessage onMessage(GlyphConfigPacket message, MessageContext ctx) {
        // 使用 1.7.10 兼容的主线程调度
        Minecraft.getMinecraft().func_152344_a(() -> {
            NecroTempus.getInstance().getLogger().info("收到配置数据: " + message.getConfigData());
            NecroTempus.getInstance().getGlyphManager().handleConfigUpdate(message.getConfigData());
        });
        return null;
    }
}
