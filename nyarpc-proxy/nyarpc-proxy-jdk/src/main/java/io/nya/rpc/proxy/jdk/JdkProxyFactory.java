package io.nya.rpc.proxy.jdk;

import io.nya.rpc.proxy.api.BaseProxyFactory;
import io.nya.rpc.proxy.api.ProxyFactory;
import io.nya.rpc.proxy.api.consumer.Consumer;
import io.nya.rpc.proxy.api.object.ObjectProxy;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

@SPIClass
public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(JdkProxyFactory.class);
    public <T> T getProxy(Class<T> clazz) {
        logger.info("基于JDK的动态代理");
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                objectProxy);
    }
}
