package io.nya.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.nya.rpc.common.utils.SerializationUtils;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.serialization.api.Serialization;

import java.nio.charset.StandardCharsets;



public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec{

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        RpcHeader header = msg.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        // TODO 可接入其他序列化方式，目前暂时只支持jdk序列化
        Serialization serialization = getJdkSerialization();
        String serializationType = header.getSerializationType();
        byteBuf.writeBytes(SerializationUtils.paddingString(serializationType).getBytes(StandardCharsets.UTF_8));

        byte[] data = serialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}