package io.nya.rpc.test.provider.service;

import io.nya.rpc.annotation.RpcService;

/**
 * DemoService 实现类
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.nya.rpc.test.provider.service.DemoService",
version = "1.0.1", group = "nya")
public class ProviderDemoServiceImpl implements DemoService{
}
