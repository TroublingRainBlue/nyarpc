package io.nya.rpc.test.api;

import io.nya.rpc.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.nya.rpc.test.api.DemoService", group = "nya", version = "1.0.0", weight = 10)
public class DemoServiceImpl implements DemoService{

    private final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("Receive name:==>>>{}", name);
        return "hello,"+name;
    }
}
