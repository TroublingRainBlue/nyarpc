package io.nya.rpc.provider.common;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.nya.rpc.common.helper.RpcServiceHelper;
import io.nya.rpc.common.threadpool.ServerThreadPool;
import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.enumerate.RpcStatus;
import io.nya.rpc.protocol.enumerate.RpcType;
import io.nya.rpc.protocol.header.RpcHeader;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> protocol) throws Exception {
        ServerThreadPool.submit(()->{
            RpcHeader header = protocol.getHeader();
            header.setMsgType((byte) RpcType.RESPONSE.getCode());
            RpcRequest request = protocol.getBody();
            LOGGER.debug("Receive requestId:{}", header.getRequestId());
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
            channelHandlerContext.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    LOGGER.info("Send Response for request:{}", header.getRequestId());
                }
            });
        });
    }

    private Object handler(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceKey = RpcServiceHelper.buildStringKey(request.getClassName(), request.getVersion(), request.getGroup());

        // 获取调用的类
        Object serviceBean = handlerMap.get(serviceKey);
        Class<?> clazz = serviceBean.getClass();

        // 获取调用方法和方法参数
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes= request.getParameterTypes();
        Object[] params = request.getParams();

        // 反射执行方法
        Method method = clazz.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, params);

    }
}
