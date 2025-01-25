package io.nya.rpc.test.registry;

import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.registry.api.RegistryService;
import io.nya.rpc.registry.api.config.RegistryConfig;
import io.nya.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ZookeeperRegistryTest {

    private RegistryService registryService;

    private ServiceMetaData serviceMeta;

    @Before
    public void init() throws Exception{
        RegistryConfig registryConfig = new RegistryConfig("127.0.0.1:2181", "zookeeper");
        this.registryService = new ZookeeperRegistryService();
        this.registryService.init(registryConfig);
        this.serviceMeta = new ServiceMetaData(ZookeeperRegistryTest.class.getName(), "1.0.0", "127.0.0.1", 8080, "nya");
    }

    @Test
    public void testRegister() throws Exception {
        this.registryService.registry(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception {
        this.registryService.unRegistry(serviceMeta);
    }

    @Test
    public void testDiscovery() throws Exception {
        this.registryService.discover(RegistryService.class.getName(), 0);
    }

    @Test
    public void testDestroy() throws Exception {
        this.registryService.destroy();
    }
}
