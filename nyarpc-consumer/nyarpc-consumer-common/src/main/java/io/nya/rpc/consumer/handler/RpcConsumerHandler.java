package io.nya.rpc.consumer.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandler.class);
    private SocketAddress remotePeer;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> protocol) throws Exception {
        LOGGER.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) {
        LOGGER.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol);
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
