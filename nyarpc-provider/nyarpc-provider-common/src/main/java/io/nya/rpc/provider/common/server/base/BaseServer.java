package io.nya.rpc.provider.common.server.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.nya.rpc.codec.RpcDecoder;
import io.nya.rpc.codec.RpcEncoder;
import io.nya.rpc.provider.common.RpcProviderHandler;
import io.nya.rpc.provider.common.server.api.Server;
import io.nya.rpc.registry.api.RegistryService;
import io.nya.rpc.registry.api.config.RegistryConfig;
import io.nya.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BaseServer implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServer.class);

    // 主机域名或者IP地址
    protected String host = "127.0.0.1";

    // 端口号
    protected int port = 20020;

    // 反射类型
    protected String reflectType = "jdk";

    protected Map<String, Object> handlerMap = new HashMap<>();

    // 注册中心相关服务
    protected RegistryService registryService;

    public BaseServer(String serverAddress, String reflectType, String registryAddr, String registryType) {
        if(!serverAddress.isEmpty()) {
            String[] server = serverAddress.split(":");
            this.host = server[0];
            this.port = Integer.parseInt(server[1]);
        }
        this.reflectType = reflectType;
        this.registryService = getRegistryService(registryAddr, registryType);
    }

    @Override
    public void startNettyServer() {
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossgroup, workergroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcProviderHandler(handlerMap, reflectType));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            LOGGER.info("Server start on {}:{}", host, port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("RPC server start error===>>>{}", e.toString());
        } finally {
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }

    private RegistryService getRegistryService(String registryAddr, String registryType) {
        // TODO 考虑SPI扩展支持多种注册中心
        RegistryService registryService = null;
        try {
            registryService = new ZookeeperRegistryService();;
            RegistryConfig config = new RegistryConfig(registryAddr, registryType);
            registryService.init(config);
        } catch (Exception e) {
            LOGGER.error("RPC init server error:{}", e.toString());
        }
        return registryService;
    }
}
