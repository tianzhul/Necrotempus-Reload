package io.github.cruciblemc.necrotempus.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.github.cruciblemc.necrotempus.NecroTempus;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class GlyphConfigPacket implements IMessage {
    private JsonObject configData;

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            // 读取 JSON 数据长度
            int length = buf.readInt();
            // 分配字节数组
            byte[] jsonBytes = new byte[length];
            // 读取数据
            buf.readBytes(jsonBytes);
            // 转换为 JSON
            String json = new String(jsonBytes, StandardCharsets.UTF_8);
            this.configData = new JsonParser().parse(json).getAsJsonObject();
        } catch (Exception e) {
            NecroTempus.getInstance().getLogger().error("解析 Glyph 配置失败", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String json = configData.toString();
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        // 写入 JSON 数据长度
        buf.writeInt(jsonBytes.length);
        // 写入 JSON 数据
        buf.writeBytes(jsonBytes);
    }

    public JsonObject getConfigData() {
        return configData;
    }
}
