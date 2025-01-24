package io.nya.rpc.test.provider.test;

import io.nya.rpc.provider.RpcSingleServer;
import org.junit.Test;
public class RpcSingleServerTest {
    @Test
    public void startRpcSingleServer() {
        RpcSingleServer server = new RpcSingleServer("127.0.0.1:27880", "io.nya.rpc.test", "cglib", "127.0.0.1:2181", "zookeeper");
        server.startNettyServer();
    }
}
