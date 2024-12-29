package io.nya.rpc.provider.common;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.enumerate.RpcType;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> protocol) throws Exception {
        LOGGER.info("Provider receive message===>>>{}", JSONObject.toJSONString(protocol));
        LOGGER.info("HandlerMap ===>>>");
        for(Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            LOGGER.info("{}==={}", entry.getKey(), entry.getValue());
        }
        RpcHeader header = protocol.getHeader();
        RpcRequest request = protocol.getBody();

        // 构建响应协议
        header.setMsgType((byte) RpcType.RESPONSE.getCode());
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();
        response.setResult("数据交互成功");
        response.setAsync(request.getAsync());
        response.setOneway(request.getOneway());
        responseRpcProtocol.setBody(response);
        responseRpcProtocol.setHeader(header);
        channelHandlerContext.writeAndFlush(responseRpcProtocol);
    }
}
