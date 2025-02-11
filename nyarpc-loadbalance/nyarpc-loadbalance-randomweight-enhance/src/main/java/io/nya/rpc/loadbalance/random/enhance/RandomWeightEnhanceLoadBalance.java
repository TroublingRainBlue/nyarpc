package io.nya.rpc.loadbalance.random.enhance;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.loadbalance.api.base.BaseEnhanceLoadBalance;
import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

@SPIClass
public class RandomWeightEnhanceLoadBalance<T> extends BaseEnhanceLoadBalance {
    private final Logger logger = LoggerFactory.getLogger(RandomWeightEnhanceLoadBalance.class);

    @Override
    public ServiceMetaData select(List<ServiceMetaData> serviceList, int hashcode, String ip) {
        logger.info("基于增强型加权随机负载均衡策略...");
        serviceList = getWeightServiceMetaList(serviceList);
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(serviceList.size());
        return serviceList.get(index);
    }
}
