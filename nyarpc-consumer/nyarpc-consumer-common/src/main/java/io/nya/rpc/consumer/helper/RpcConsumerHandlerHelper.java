package io.nya.rpc.consumer.helper;


import io.nya.rpc.consumer.handler.RpcConsumerHandler;
import io.nya.rpc.protocol.meta.ServiceMetaData;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumerHandlerHelper {
    private static Map<String, RpcConsumerHandler> rpcConsumerHandlerMap = new ConcurrentHashMap<>();

    private static String getKey(ServiceMetaData key){
        return key.getServiceAddr().concat("_").concat(String.valueOf(key.getServicePort()));
    }

    public static void put(ServiceMetaData key, RpcConsumerHandler value){
        rpcConsumerHandlerMap.put(getKey(key), value);
    }

    public static RpcConsumerHandler get(ServiceMetaData key){
        return rpcConsumerHandlerMap.get(getKey(key));
    }

    public static void closeRpcClientHandler(){
        Collection<RpcConsumerHandler> rpcClientHandlers = rpcConsumerHandlerMap.values();
        if (rpcClientHandlers != null){
            rpcClientHandlers.stream().forEach((rpcClientHandler) -> {
                rpcClientHandler.close();
            });
        }
        rpcConsumerHandlerMap.clear();
    }
}
