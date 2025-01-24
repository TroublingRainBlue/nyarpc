package io.nya.rpc.proxy.api.consumer;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.registry.api.RegistryService;

public interface Consumer {
    /**
     * 发送请求
     */
    RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService)throws Exception;
}
