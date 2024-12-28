package io.nya.rpc.protocol.enumerate;

public enum RpcType {
    // 请求消息
    REQUEST(1),
    // 响应消息
    RESPONSE(2),
    // 心跳消息
    HEARTBEAT(3);

    private final int type;

    RpcType(int code) {
        this.type = code;
    }

    public static RpcType findType(byte msgType) {
        if(msgType == 1) return REQUEST;
        else if(msgType == 2) return RESPONSE;
        else if(msgType == 3) return HEARTBEAT;
        else return null;
    }

    public int getCode() {
        return type;
    }
}
