package io.nya.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.nya.rpc.consumer.common.threadpool.ClientThreadPool;
import io.nya.rpc.consumer.handler.RpcConsumerHandler;
import io.nya.rpc.consumer.initializer.RpcConsumerInitializer;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.proxy.api.consumer.Consumer;
import io.nya.rpc.proxy.api.future.RpcFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumer implements Consumer {
    private static final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;

    private static volatile RpcConsumer instance;
    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();
    private RpcConsumer() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(4);
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new RpcConsumerInitializer());
    }
    // 实现单例模式
    public static RpcConsumer getInstance() {
        if(instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }
        }
        return instance;
    }

    public void close() {
        group.shutdownGracefully();
        ClientThreadPool.shutdown();
    }

    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
        // TODO 从注册中心获得服务提供者的 IP 和 Port，现在暂时写死
        String serverAddress = "127.0.0.1";
        int port = 27880;
        String key = serverAddress.concat("_").concat(String.valueOf(port));
        RpcConsumerHandler handler = handlerMap.get(key);
        // 缓存中没有ClientHandler
        if(handler == null) {
            handler = getRpcConsumerHandler(serverAddress, port);
            handlerMap.put(key, handler);
        } else if(!handler.getChannel().isActive()) {
            handler.close();
            handler = getRpcConsumerHandler(serverAddress, port);
            handlerMap.put(key, handler);
        }
        RpcRequest request = protocol.getBody();
        return handler.sendRequest(protocol, request.getAsync(), request.getOneway());
    }

    // 建立连接并返回对应ClientHandler
    private RpcConsumerHandler getRpcConsumerHandler(String serverAddress, int port) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(serverAddress, port).sync();
        future.addListener((ChannelFutureListener) listener->{
            if(future.isSuccess()) {
                logger.info("connect rpc server {} on port {} success.", serverAddress, port);
            } else {
                logger.error("connect rpc server {} on port {} failed: {}", serverAddress, port, future.cause().toString());
            }
        });
        return future.channel().pipeline().get(RpcConsumerHandler.class);
    }
}
