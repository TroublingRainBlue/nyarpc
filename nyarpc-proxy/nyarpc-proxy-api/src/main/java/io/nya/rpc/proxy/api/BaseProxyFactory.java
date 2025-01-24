package io.nya.rpc.proxy.api;

import io.nya.rpc.proxy.api.config.ProxyConfig;
import io.nya.rpc.proxy.api.object.ObjectProxy;

public abstract class BaseProxyFactory<T> implements ProxyFactory{
    protected ObjectProxy<T> objectProxy;
    @Override
    public <T> void init(ProxyConfig<T> config) {
        this.objectProxy = new ObjectProxy(config.getClazz(),
                config.getServiceVersion(),
                config.getServiceGroup(),
                config.getTimeout(),
                config.getConsumer(),
                config.getSerializationType(),
                config.getAsync(),
                config.getOneway(),
                config.getRegistryService());
    }
}
