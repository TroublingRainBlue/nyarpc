package io.nya.rpc.proxy.api.object;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeaderFactory;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.proxy.api.consumer.Consumer;
import io.nya.rpc.proxy.api.future.RpcFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ObjectProxy<T> implements InvocationHandler {
    private final static Logger logger = LoggerFactory.getLogger(ObjectProxy.class);

    /**
     * 接口的Class对象
     */
    private Class<T> clazz;

    /**
     * 服务版本号
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 超时时间
     */
    private long timeout = 15000;

    /**
     * 服务消费者
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            switch (name) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler" + this;
                default:
                    throw new IllegalStateException(String.valueOf(method));
            }
        }
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRpcHeader(this.serializationType));
        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setGroup(this.serviceGroup);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        request.setAsync(this.async);
        request.setOneway(this.oneway);
        protocol.setBody(request);

        logger.debug(method.getDeclaringClass().getName());
        logger.debug(method.getName());

        RpcFuture future = this.consumer.sendRequest(protocol);
        return future == null ? null : timeout > 0 ? future.get(timeout, TimeUnit.MILLISECONDS) : future.get();
    }
}
