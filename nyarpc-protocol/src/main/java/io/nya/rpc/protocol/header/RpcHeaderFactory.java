package io.nya.rpc.protocol.header;

import io.nya.rpc.constants.RpcConstants;
import io.nya.rpc.protocol.enumerate.RpcType;

public class RpcHeaderFactory {
    public static RpcHeader getRpcHeader(String serializtionType) {
        RpcHeader header = new RpcHeader();
        long requestId = IdFactory.getInstance().getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) RpcType.REQUEST.getCode());
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializtionType);
        return header;
    }
}
