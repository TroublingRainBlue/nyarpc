package io.nya.rpc.proxy.jdk;

import io.nya.rpc.proxy.api.BaseProxyFactory;
import io.nya.rpc.proxy.api.ProxyFactory;
import io.nya.rpc.proxy.api.consumer.Consumer;
import io.nya.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;


public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                objectProxy);
    }
}
