package io.nya.rpc.consumer;

import io.nya.rpc.consumer.common.RpcConsumer;
import io.nya.rpc.proxy.api.ProxyFactory;
import io.nya.rpc.proxy.api.async.IAsyncObjectProxy;
import io.nya.rpc.proxy.api.config.ProxyConfig;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.proxy.api.object.ObjectProxy;
import io.nya.rpc.proxy.jdk.JdkProxyFactory;
import io.nya.rpc.registry.api.RegistryService;
import io.nya.rpc.registry.api.config.RegistryConfig;
import io.nya.rpc.registry.zookeeper.ZookeeperRegistryService;
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

    /**
     * 注册中心
     */
    private RegistryService registryService;

    public RpcClient(String registryAddr, String registryType ,String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.oneway = oneway;
        this.timeout = timeout;
        this.serviceVersion = serviceVersion;
        this.serializationType = serializationType;
        this.serviceGroup = serviceGroup;
        this.async = async;
        this.registryService = getRegistryService(registryAddr, registryType);
    }

    public <T> T create(Class<T> interfaceClass) {
        ProxyFactory proxyFactory = new JdkProxyFactory<T>();
        proxyFactory.init(new ProxyConfig<>(interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway, registryService));
        return proxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy creatAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T> (interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway, registryService);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }

    private RegistryService getRegistryService(String registryAddr, String registryType) {
        // TODO 考虑SPI扩展支持多种注册中心
        RegistryService registryService = null;
        try {
            registryService = new ZookeeperRegistryService();;
            RegistryConfig config = new RegistryConfig(registryAddr, registryType);
            registryService.init(config);
        } catch (Exception e) {
            logger.error("RPC init server error:{}", e.toString());
        }
        return registryService;
    }
}
