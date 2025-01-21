package io.nya.rpc.proxy.api;

import io.nya.rpc.proxy.api.config.ProxyConfig;

public interface ProxyFactory {

    <T> T getProxy(Class<T> clazz);

    default <T> void init(ProxyConfig<T> proxyConfig){}
}

