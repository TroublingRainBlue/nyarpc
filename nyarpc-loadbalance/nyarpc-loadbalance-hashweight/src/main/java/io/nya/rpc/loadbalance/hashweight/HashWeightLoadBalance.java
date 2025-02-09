package io.nya.rpc.loadbalance.hashweight;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SPIClass
public class HashWeightLoadBalance<T> implements LoadBalance<T> {
    private final Logger logger = LoggerFactory.getLogger(HashWeightLoadBalance.class);

    @Override
    public T select(List<T> serviceList, int hashcode) {
        logger.info("基于加权Hash的负载均衡策略...");
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        hashcode = Math.abs(hashcode);
        int count = hashcode % serviceList.size();
        if(count <= 1) {
            count = serviceList.size();
        }
        return serviceList.get(hashcode % count);
    }
}
