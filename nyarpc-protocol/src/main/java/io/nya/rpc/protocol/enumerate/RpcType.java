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

    public int getCode() {
        return type;
    }
}
