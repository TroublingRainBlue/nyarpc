package io.nya.rpc.consumer.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.nya.rpc.consumer.common.context.RpcContext;
import io.nya.rpc.proxy.api.future.RpcFuture;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    // private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandler.class);
    private SocketAddress remotePeer;
    private Channel channel;
    private Map<Long, RpcFuture> penddingMap = new ConcurrentHashMap<>();
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
        // 得到结果后添加进Map
        Long requestId = protocol.getHeader().getRequestId();
        RpcFuture future = penddingMap.remove(requestId);
        if(future != null) {
            future.done(protocol);
        }
    }

    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, boolean async, boolean oneway) {
        return oneway ? this.sendRequestOneway(protocol) : async ? this.sendRequestAsync(protocol) : this.sendRequestSync(protocol);
    }

    /**
     * 服务消费者进行同步调用
     */
    public RpcFuture sendRequestSync(RpcProtocol<RpcRequest> protocol) {
        // LOGGER.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        RpcFuture future = this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return future;
    }

    /**
     * 服务消费者进行异步调用
     */
    public RpcFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol) {
        RpcFuture future = this.getRpcFuture(protocol);
        // 如果为异步调用，将Future放入Context中
        RpcContext.getInstance().setRpcFuture(future);
        channel.writeAndFlush(protocol);
        return null;
    }

    /**
     * 服务消费者进行单向调用
     */
    public RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
        channel.writeAndFlush(protocol);
        return null;
    }
    private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
        RpcFuture future = new RpcFuture(protocol);
        penddingMap.put(protocol.getHeader().getRequestId(), future);
        return future;
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
