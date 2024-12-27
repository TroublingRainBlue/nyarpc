package io.nya.rpc.protocol.response;

import io.nya.rpc.protocol.base.RpcMessage;

public class RpcResponse extends RpcMessage {
    private static final long serialVersionUID= 425335064405584525L;
    private String error;
    private Object result;

    public void setError(String error) {
        this.error = error;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return this.result;
    }

    public String getError() {
        return this.error;
    }
}
