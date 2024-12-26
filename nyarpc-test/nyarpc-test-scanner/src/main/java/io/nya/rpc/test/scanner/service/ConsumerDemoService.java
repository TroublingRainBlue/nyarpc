package io.nya.rpc.test.scanner.service;

import io.nya.rpc.annotation.RpcReference;

public class ConsumerDemoService implements ConsumerService{

    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1", group = "nya",
    version = "1.0.1")
    private DemoService demoService;
}
