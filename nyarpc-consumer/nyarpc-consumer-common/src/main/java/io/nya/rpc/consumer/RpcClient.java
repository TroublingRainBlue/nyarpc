package io.nya.rpc.consumer;

import io.nya.rpc.consumer.common.RpcConsumer;
import io.nya.rpc.proxy.api.ProxyFactory;
import io.nya.rpc.proxy.api.async.IAsyncObjectProxy;
import io.nya.rpc.proxy.api.config.ProxyConfig;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.proxy.api.object.ObjectProxy;
import io.nya.rpc.proxy.jdk.JdkProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient {
    private final static Logger logger = LoggerFactory.getLogger(RpcClient.class);
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

    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.oneway = oneway;
        this.timeout = timeout;
        this.serviceVersion = serviceVersion;
        this.serializationType = serializationType;
        this.serviceGroup = serviceGroup;
        this.async = async;
    }

    public <T> T create(Class<T> interfaceClass) {
        ProxyFactory proxyFactory = new JdkProxyFactory<T>();
        proxyFactory.init(new ProxyConfig<>(interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway));
        return proxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy creatAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T> (interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}
