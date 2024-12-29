package io.nya.rpc.test.consumer.codec.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.nya.rpc.codec.RpcDecoder;
import io.nya.rpc.codec.RpcEncoder;
import io.nya.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;

public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipe = socketChannel.pipeline();
        pipe.addLast(new RpcEncoder());
        pipe.addLast(new RpcDecoder());
        pipe.addLast(new RpcTestConsumerHandler());
    }
}
