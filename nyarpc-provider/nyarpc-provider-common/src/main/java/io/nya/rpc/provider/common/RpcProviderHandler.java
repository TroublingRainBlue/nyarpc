package io.nya.rpc.provider.common;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.nya.rpc.common.helper.RpcServiceHelper;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.enumerate.RpcStatus;
import io.nya.rpc.protocol.enumerate.RpcType;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import io.nya.rpc.reflect.api.ReflectInvoker;
import io.nya.rpc.spi.ExtensionLoader;
import io.nya.rpc.threadpool.ServerThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    private ReflectInvoker reflectInvoker;

    public RpcProviderHandler(Map<String, Object> handlerMap, String reflectType) {
        this.handlerMap = handlerMap;
        this.reflectInvoker = ExtensionLoader.getExtension(ReflectInvoker.class, reflectType);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> protocol) throws Exception {

        ServerThreadPool.submit(()->{
            RpcHeader header = protocol.getHeader();
            header.setMsgType((byte) RpcType.RESPONSE.getCode());
            RpcRequest request = protocol.getBody();
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();

            // 处理请求信息
            try {
                Object result = handler(request);
                response.setOneway(request.getOneway());
                response.setAsync(request.getAsync());
                response.setResult(result);
                header.setStatus((byte) RpcStatus.SUCCESS.getCode());
            } catch (Throwable t) {
                response.setError(t.toString());
                header.setStatus((byte) RpcStatus.FALL.getCode());
                LOGGER.error("Rpc Server Handle Request error:{}", t.toString());
            }

            responseRpcProtocol.setBody(response);
            responseRpcProtocol.setHeader(header);
            channelHandlerContext.writeAndFlush(responseRpcProtocol).addListener((ChannelFutureListener)
                    channelFuture -> LOGGER.info("Send Response for request:{}", header.getRequestId()));
        });
    }

    private Object handler(RpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildStringKey(request.getClassName(), request.getVersion(), request.getGroup());

        // 获取调用的类
        Object serviceBean = handlerMap.get(serviceKey);
        Class<?> clazz = serviceBean.getClass();

        // 获取调用方法和方法参数
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes= request.getParameterTypes();
        Object[] params = request.getParams();

        return reflectInvoker.invokeMethod(serviceBean, clazz, methodName, parameterTypes, params);
    }
}
