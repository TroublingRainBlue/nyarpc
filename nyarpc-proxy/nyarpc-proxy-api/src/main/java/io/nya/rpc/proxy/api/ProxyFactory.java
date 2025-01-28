package io.nya.rpc.proxy.api;

import io.nya.rpc.proxy.api.config.ProxyConfig;
import io.nya.rpc.spi.annotation.SPI;

@SPI
public interface ProxyFactory {

    <T> T getProxy(Class<T> clazz);

    default <T> void init(ProxyConfig<T> proxyConfig){}
}

