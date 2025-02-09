package io.nya.rpc.loadbalance.randomweight;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


@SPIClass
public class RandomWeightLoadBalance<T> implements LoadBalance<T> {
    private final Logger logger = LoggerFactory.getLogger(RandomWeightLoadBalance.class);
    @Override
    public T select(List<T> servers, int hashCode) {
        logger.info("基于加权随机算法的负载均衡策略...");
        if (servers == null || servers.isEmpty()){
            return null;
        }
        hashCode = Math.abs(hashCode);
        int count = hashCode % servers.size();
        if (count <= 1){
            count = servers.size();
        }
        Random random = new Random();
        int index = random.nextInt(count);
        return servers.get(index);
    }
}
