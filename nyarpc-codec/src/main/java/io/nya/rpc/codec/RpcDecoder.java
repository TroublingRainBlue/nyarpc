package io.nya.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.nya.rpc.common.utils.SerializationUtils;
import io.nya.rpc.constants.RpcConstants;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.enumerate.RpcType;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import io.nya.rpc.serialization.api.Serialization;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 判断是否为协议内容
        if(byteBuf.readableBytes() < RpcConstants.HEADER_TOTAL_LEN) {
            return;
        }
        byteBuf.markReaderIndex();
        // 解析 header 是否符合协议规定
        short magic = byteBuf.readShort();
        if(magic != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal " + magic);
        }
        byte msgType = byteBuf.readByte();
        byte status = byteBuf.readByte();
        long requestId = byteBuf.readLong();
        ByteBuf serializationt = byteBuf.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_LEN);
        String serializationType = SerializationUtils.subString(serializationt.toString(StandardCharsets.UTF_8));
        // 读取body
        int dataLength = byteBuf.readInt();
        if(byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        // 判断消息类型是否合法
        RpcType msgTypeEnum = RpcType.findType(msgType);
        if(msgTypeEnum == null) return;

        // 获得 header
        RpcHeader header = new RpcHeader();
        header.setMagic(magic);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setSerializationType(serializationType);
        header.setMsgLen(dataLength);
        header.setMsgType(msgType);
        Serialization serialization = getSerialization(serializationType);

        // 根据消息类型解析body
        switch (msgTypeEnum) {
            case REQUEST: {
                RpcRequest request = serialization.deserialize(data, RpcRequest.class);
                if(request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    list.add(protocol);
                }
                break;
            }
            case RESPONSE: {
                RpcResponse response = serialization.deserialize(data, RpcResponse.class);
                if(response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    list.add(protocol);
                }
                break;
            }
            case HEARTBEAT: {
                // TODO
                break;
            }
        }
    }
}
