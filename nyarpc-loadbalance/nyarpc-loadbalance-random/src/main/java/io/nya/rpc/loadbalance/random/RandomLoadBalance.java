package io.nya.rpc.loadbalance.random;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

@SPIClass
public class RandomLoadBalance<T> implements LoadBalance<T> {
    private final static Logger logger = LoggerFactory.getLogger(RandomLoadBalance.class);

    @Override
    public T select(List<T> serviceList, int hashcode, String ip) {
        logger.info("基于随机算法的负载均衡");

        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(serviceList.size());
        return serviceList.get(index);
    }
}
