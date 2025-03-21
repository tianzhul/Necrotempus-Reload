package io.github.cruciblemc.necrotempus.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class GlyphConfigPacket implements IMessage {
    private JsonObject configData;

    // 必须有无参构造
    public GlyphConfigPacket() {}

    public GlyphConfigPacket(JsonObject configData) {
        this.configData = configData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String json = ByteBufUtils.readUTF8String(buf);
        this.configData = new JsonParser().parse(json).getAsJsonObject();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, configData.toString());
    }

}