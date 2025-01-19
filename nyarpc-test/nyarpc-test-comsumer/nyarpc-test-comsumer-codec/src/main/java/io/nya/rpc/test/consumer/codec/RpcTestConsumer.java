package io.nya.rpc.test.consumer.codec;

import io.nya.rpc.consumer.common.RpcConsumer;
import io.nya.rpc.proxy.api.callback.AsyncRpcCallback;
import io.nya.rpc.proxy.api.future.RpcFuture;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.header.RpcHeaderFactory;
import io.nya.rpc.protocol.request.RpcRequest;

public class RpcTestConsumer {
    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RpcFuture future = consumer.sendRequest(getRpcConsumerProtocol());
        future.addCallback(new AsyncRpcCallback() {
            @Override
            public void onSuccess(Object result) {
                System.out.println("从服务消费者获取到的数据===>>>" + result);
            }

            @Override
            public void onException(Exception e) {
                System.out.println("抛出异常===>>>" +e);
            }
        });
        Thread.sleep(2000);
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
        request.setOneway(false);
        request.setMethodName("hello");
        protocol.setHeader(header);
        protocol.setBody(request);
        return  protocol;
    }
}
