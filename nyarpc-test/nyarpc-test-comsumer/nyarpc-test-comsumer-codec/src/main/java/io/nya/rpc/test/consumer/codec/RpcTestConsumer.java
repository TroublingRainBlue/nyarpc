package io.nya.rpc.test.consumer.codec;

import com.alibaba.fastjson2.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.nya.rpc.consumer.common.RpcConsumer;
import io.nya.rpc.consumer.common.RpcContext;
import io.nya.rpc.consumer.common.RpcFuture;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.header.RpcHeaderFactory;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;

public class RpcTestConsumer {
    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        consumer.sendRequest(getRpcConsumerProtocol());
        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcConsumerProtocol() {
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        RpcHeader header = RpcHeaderFactory.getRpcHeader("jdk");
        RpcRequest request = new RpcRequest();
        request.setClassName("io.nya.rpc.test.api.DemoService");
        request.setGroup("nya");
        request.setParams(new Object[]{"Sakiko"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(true);
        request.setMethodName("hello");
        protocol.setHeader(header);
        protocol.setBody(request);
        return  protocol;
    }
}
