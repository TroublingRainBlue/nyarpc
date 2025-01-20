package io.nya.rpc.test.consumer;

import io.nya.rpc.consumer.RpcClient;
import io.nya.rpc.test.api.DemoService;

public class ConsumerNativeTest {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("1.0.0", "nya", "jdk", 2000, false, false);
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("Nagisaki Soyo");
        System.out.println("返回的结果===》" + result);
        rpcClient.shutdown();
    }
}
