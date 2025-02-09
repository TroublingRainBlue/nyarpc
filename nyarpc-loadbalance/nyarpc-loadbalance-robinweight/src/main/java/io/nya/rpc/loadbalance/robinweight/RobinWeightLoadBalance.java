package io.nya.rpc.loadbalance.robinweight;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SPIClass
public class RobinWeightLoadBalance<T> implements LoadBalance<T> {
    private final Logger logger = LoggerFactory.getLogger(RobinWeightLoadBalance.class);
    private AtomicInteger cur = new AtomicInteger(0);
    @Override
    public T select(List<T> serviceList, int hashcode, String ip) {
        logger.info("基于加权轮询的负载均衡策略...");
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        hashcode = Math.abs(hashcode);
        int count = hashcode % serviceList.size();
        int index = cur.getAndIncrement();
        if(index >= Integer.MAX_VALUE - 100000) {
            cur.set(0);
        }
        if (count <= 1){
            count = serviceList.size();
        }
        return serviceList.get(index % count);
    }
}
