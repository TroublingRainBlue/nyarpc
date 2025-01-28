package io.nya.rpc.proxy.jdk;

import io.nya.rpc.proxy.api.BaseProxyFactory;
import io.nya.rpc.proxy.api.ProxyFactory;
import io.nya.rpc.spi.annotation.SPIClass;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SPIClass
public class CGlibProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(CGlibProxyFactory.class);
    public <T> T getProxy(Class<T> clazz) {
        logger.info("基于CGlib的动态代理");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return objectProxy.invoke(o, method, objects);
            }
        });
        return (T) enhancer.create();
    }
}
