package io.nya.rpc.protocol.base;

import java.io.Serializable;

public class RpcMessage implements Serializable {
    // 是否单向调用
    private boolean oneway;
    // 是否异步调用
    private boolean async;

    // getter、setter
    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean getAsync() {
        return this.async;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean getOnway() {
        return this.oneway;
    }
}
