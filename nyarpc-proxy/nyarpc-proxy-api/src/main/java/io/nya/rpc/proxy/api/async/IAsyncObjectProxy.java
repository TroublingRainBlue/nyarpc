package io.nya.rpc.proxy.api.async;

import io.nya.rpc.proxy.api.future.RpcFuture;

public interface IAsyncObjectProxy {
    public RpcFuture call(String methodName, Object... args);
}
