package io.nya.rpc.loadbalance.api.helper;

import io.nya.rpc.protocol.meta.ServiceMetaData;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceMetaListHelper {
    private static volatile List<ServiceMetaData> cacheList = new CopyOnWriteArrayList<>();

    public static List<ServiceMetaData> getServiceMetaList(List<ServiceInstance<ServiceMetaData>> serviceList) {
        if(serviceList == null || serviceList.isEmpty()) {
            return cacheList;
        }
        cacheList.clear();
        serviceList.forEach((instance)->{
            cacheList.add(instance.getPayload());
        });
        return cacheList;
    }
}
