package io.nya.rpc.loadbalance.hash;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SPIClass
public class HashLoadBalance<T> implements LoadBalance<T> {

    private final Logger logger = LoggerFactory.getLogger(HashLoadBalance.class);
    @Override
    public T select(List<T> serviceList, int hashcode, String ip) {
        logger.info("基于Hash的负载均衡策略...");
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        return serviceList.get(Math.abs(hashcode) % serviceList.size());
    }
}
