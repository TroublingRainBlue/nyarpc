package io.nya.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.header.RpcHeaderFactory;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcTestConsumerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        LOGGER.info("收到响应结果:===>>>{}", JSONObject.toJSONString(rpcResponseRpcProtocol));
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        LOGGER.info("开始发送数据");
        // 模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        RpcHeader header = RpcHeaderFactory.getRpcHeader("jdk");
        RpcRequest request = new RpcRequest();
        request.setClassName("io.nya.rpc.test.provider.service.DemoService");
        request.setGroup("nya");
        request.setParams(new Object[]{"nya"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        request.setMethodName("Hello");
        protocol.setHeader(header);
        protocol.setBody(request);
        LOGGER.info("发送的数据为:===>>>{}", JSONObject.toJSONString(protocol));
        channelHandlerContext.writeAndFlush(protocol);
        LOGGER.info("数据发送完毕...");

    }
}
