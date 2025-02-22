package io.nya.rpc.test.consumer;

import io.nya.rpc.constants.RpcConstants;
import io.nya.rpc.consumer.RpcClient;
import io.nya.rpc.proxy.api.async.IAsyncObjectProxy;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.test.api.DemoService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class ConsumerNativeTest {
    RpcClient client;

    @Before
    public void clientInit() {
        this.client = new RpcClient("127.0.0.1:2181", "zookeeper", RpcConstants.LOADBALANCE_RANDOM_WEIGHT_ENHANCE, RpcConstants.PROXY_JAVASSIST,"1.0.0", "nya", RpcConstants.SERIALIZATION_PROTOSTUFF, 2000, false, false);
    }

    @Test
    public void testInterfaceRpc() {
        DemoService demoService = client.create(DemoService.class);
        String result = demoService.hello("Nagasaki Soyo");
        System.out.println("result ===>>" + result);
        client.shutdown();
    }

    @Test
    public void testInterfaceRpcAsync() throws ExecutionException, InterruptedException {
        IAsyncObjectProxy demoService = client.creatAsync(DemoService.class);
        RpcFuture future = demoService.call("hello", "sakiko");
        System.out.println("result ===>>" + future.get());
        client.shutdown();
    }
}
