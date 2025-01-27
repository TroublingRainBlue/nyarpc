package io.nya.rpc.test.consumer;

import io.nya.rpc.consumer.RpcClient;
import io.nya.rpc.proxy.api.async.IAsyncObjectProxy;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.test.api.DemoService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class ConsumerNativeTest {
    RpcClient client;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RpcClient rpcClient = new RpcClient("127.0.0.1:2181", "zookeeper","1.0.0", "nya", "jdk", 2000, false, false);

        IAsyncObjectProxy demoService = rpcClient.creatAsync(DemoService.class);
        RpcFuture future = demoService.call("hello", "Nagasaki Soyo");
        System.out.println("返回的结果===》" + future.get());
        rpcClient.shutdown();
    }

    @Before
    public void clientInit() {
        this.client = new RpcClient("127.0.0.1:2181", "zookeeper","1.0.0", "nya", "json", 2000, false, false);
    }

    @Test
    public void testInterfaceRpc() {
        DemoService demoService = client.create(DemoService.class);
        String result = demoService.hello("Soyo Nagasaki");
        System.out.println("返回的结果===》" + result);
        client.shutdown();
    }

    @Test
    public void testInterfaceRpcAsync() throws ExecutionException, InterruptedException {
        IAsyncObjectProxy demoService = client.creatAsync(DemoService.class);
        RpcFuture future = demoService.call("hello", "sakiko");
        System.out.println("返回的结果===》" + future.get());
        client.shutdown();
    }
}
