package io.nya.rpc.loadbalance.robin;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SPIClass
public class RobinLoadBalance<T> implements LoadBalance<T> {
    private final Logger logger = LoggerFactory.getLogger(RobinLoadBalance.class);
    private AtomicInteger cur = new AtomicInteger(0);

    @Override
    public T select(List<T> serviceList, int hashcode) {
        logger.info("基于轮询的负载均衡策略...");
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        int index = cur.getAndIncrement();
        if(index >= Integer.MAX_VALUE - 100000) {
            cur.set(0);
        }
        return serviceList.get(index % serviceList.size());
    }
}
