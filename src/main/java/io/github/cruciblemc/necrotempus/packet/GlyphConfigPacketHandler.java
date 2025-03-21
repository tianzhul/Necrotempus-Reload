package io.github.cruciblemc.necrotempus.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.github.cruciblemc.necrotempus.NecroTempus;
import io.github.cruciblemc.necrotempus.packet.GlyphConfigPacket;

public class GlyphConfigPacketHandler implements IMessageHandler<GlyphConfigPacket, IMessage> {
    @Override
    public IMessage onMessage(GlyphConfigPacket message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            NecroTempus.getInstance().getGlyphManager()
                    .handleConfigUpdate(message.getConfigData());
        }
        return null;
    }
}