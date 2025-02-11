package io.nya.rpc.registry.zookeeper;

import io.nya.rpc.common.helper.RpcServiceHelper;
import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.loadbalance.api.helper.ServiceMetaListHelper;
import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.registry.api.RegistryService;
import io.nya.rpc.registry.api.config.RegistryConfig;
import io.nya.rpc.spi.ExtensionLoader;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

public class ZookeeperRegistryService implements RegistryService {
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/nyarpc";
    private ServiceDiscovery<ServiceMetaData> serviceDiscovery;
    private LoadBalance loadBalance;
    @Override
    public void registry(ServiceMetaData serviceMetaData) throws Exception {
        ServiceInstance<ServiceMetaData> serviceInstance = ServiceInstance
                .<ServiceMetaData>builder()
                .name(RpcServiceHelper.buildStringKey(serviceMetaData.getServiceName(), serviceMetaData.getServiceVersion(), serviceMetaData.getServiceGroup()))
                .address(serviceMetaData.getServiceAddr())
                .port(serviceMetaData.getServicePort())
                .payload(serviceMetaData)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMetaData serviceMetaData) throws Exception {
        ServiceInstance<ServiceMetaData> serviceInstance = ServiceInstance.<ServiceMetaData>builder()
                .name(RpcServiceHelper.buildStringKey(serviceMetaData.getServiceName(), serviceMetaData.getServiceVersion(), serviceMetaData.getServiceGroup()))
                .address(serviceMetaData.getServiceAddr())
                .port(serviceMetaData.getServicePort())
                .payload(serviceMetaData)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMetaData discover(String serviceName, int invokeHashCode, String ip) throws Exception {
        Collection<ServiceInstance<ServiceMetaData>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return (ServiceMetaData) loadBalance.select(ServiceMetaListHelper.getServiceMetaList((List<ServiceInstance<ServiceMetaData>>) serviceInstances), invokeHashCode, ip);
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
    }

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryConfig.getRegistryAddr(), new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMetaData> serializer = new JsonInstanceSerializer<>(ServiceMetaData.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaData.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
        this.loadBalance = ExtensionLoader.getExtension(LoadBalance.class, registryConfig.getLoadBalance());
    }
}
