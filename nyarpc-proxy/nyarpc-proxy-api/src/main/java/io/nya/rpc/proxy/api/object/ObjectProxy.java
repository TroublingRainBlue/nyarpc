package io.nya.rpc.proxy.api.object;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.header.RpcHeaderFactory;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.proxy.api.async.IAsyncObjectProxy;
import io.nya.rpc.proxy.api.consumer.Consumer;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ObjectProxy<T> implements IAsyncObjectProxy, InvocationHandler {
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

    /**
     * 注册中心
     */
    private RegistryService registryService;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway, RegistryService registryService) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = registryService;
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

        RpcFuture future = this.consumer.sendRequest(protocol, registryService);
        return future == null ? null : timeout > 0 ? future.get(timeout, TimeUnit.MILLISECONDS) : future.get();
    }

    @Override
    public RpcFuture call(String methodName, Object... args) {
        RpcProtocol<RpcRequest> protocol = creatRequest(this.clazz.getName(), methodName, args);
        RpcFuture future = null;
        try {
            future = this.consumer.sendRequest(protocol, registryService);
        } catch (Exception e) {
            logger.error("async all throw exception:{}", e.toString());
        }
        return future;
    }

    private RpcProtocol<RpcRequest> creatRequest(String className, String methodName, Object... args) {
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        RpcHeader header = RpcHeaderFactory.getRpcHeader(serializationType);
        protocol.setHeader(header);
        RpcRequest request = new RpcRequest();
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setOneway(this.oneway);
        request.setAsync(this.async);
        request.setParams(args);
        request.setGroup(this.serviceGroup);
        request.setVersion(this.serviceVersion);
        Class[] parameterTypes = new Class[args.length];
        for(int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }

        request.setParameterTypes(parameterTypes);
        protocol.setBody(request);

        logger.debug(className);
        logger.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            logger.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < args.length; ++i) {
            logger.debug(args[i].toString());
        }

        return protocol;
    }

    private Class<?> getClassType(Object obj){
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName){
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }
        return classType;
    }
}
