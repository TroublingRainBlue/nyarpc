package io.nya.rpc.protocol.enumerate;

public enum RpcStatus {
    SUCCESS(1),
    FALL(0);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
