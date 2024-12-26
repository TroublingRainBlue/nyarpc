package io.nya.rpc.provider.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RpcProviderHandler extends SimpleChannelInboundHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        LOGGER.info("Provider receive message===>>>{}", o.toString());
        LOGGER.info("HandlerMap ===>>>");
        for(Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            LOGGER.info("{}==={}", entry.getKey(), entry.getValue().toString());
        }

        channelHandlerContext.writeAndFlush(0);
    }
}
