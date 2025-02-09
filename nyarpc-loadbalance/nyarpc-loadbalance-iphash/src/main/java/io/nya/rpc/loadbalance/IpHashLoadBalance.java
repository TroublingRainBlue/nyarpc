package io.nya.rpc.loadbalance;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SPIClass
public class IpHashLoadBalance<T> implements LoadBalance<T> {
    private final Logger logger = LoggerFactory.getLogger(IpHashLoadBalance.class);
    @Override
    public T select(List<T> serviceList, int hashcode, String ip) {
        logger.info("基于源ip地址hash的负载均衡策略...");
        if(serviceList == null || serviceList.isEmpty()) {
            return null;
        }
        // if ip == null, return first server
        if(ip == null) {
            return serviceList.get(0);
        }
        int index = Math.abs(ip.hashCode() + hashcode);
        return serviceList.get(index % serviceList.size());
    }
}
