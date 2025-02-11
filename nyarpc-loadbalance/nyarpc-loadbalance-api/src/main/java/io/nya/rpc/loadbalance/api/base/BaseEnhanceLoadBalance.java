package io.nya.rpc.loadbalance.api.base;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.protocol.meta.ServiceMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class BaseEnhanceLoadBalance implements LoadBalance<ServiceMetaData> {
    // 按权重分配每个服务实例的比例
    protected List<ServiceMetaData> getWeightServiceMetaList(List<ServiceMetaData> servers){
        if (servers == null || servers.isEmpty()){
            return null;
        }
        List<ServiceMetaData> serviceMetaList = new ArrayList<>();
        servers.stream().forEach((server) -> {
            IntStream.rangeClosed(0, server.getWeight()).forEach((i) -> {
                serviceMetaList.add(server);
            });
        });
        return serviceMetaList;
    }
}
