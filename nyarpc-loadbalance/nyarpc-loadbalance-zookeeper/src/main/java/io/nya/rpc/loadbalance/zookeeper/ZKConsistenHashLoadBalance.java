package io.nya.rpc.loadbalance.zookeeper;

import io.nya.rpc.loadbalance.api.LoadBalance;
import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.spi.annotation.SPIClass;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 基于zookeeper的一致性Hash负载均衡策略
 */
@SPIClass
public class ZKConsistenHashLoadBalance<T> implements LoadBalance<T> {
    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SPLIT = "#";
    private final Logger logger = LoggerFactory.getLogger(ZKConsistenHashLoadBalance.class);
    @Override
    public T select(List<T> serviceList, int hashCode, String ip) {
        logger.info("基于Zookeeper的一致性Hash算法的负载均衡策略...");
        TreeMap<Integer, T> ring = makeConsistentHashRing(serviceList);
        return allocateNode(ring, hashCode);
    }

    private T allocateNode(TreeMap<Integer, T> ring, int hashCode) {
        Map.Entry<Integer, T> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry();
        }
        if (entry == null){
            throw new RuntimeException("not discover useful service, please register service in registry center.");
        }
        return entry.getValue();
    }

    private TreeMap<Integer, T> makeConsistentHashRing(List<T> servers) {
        TreeMap<Integer, T> ring = new TreeMap<>();
        for (T instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }

    private String buildServiceInstanceKey(T instance) {
        return Objects.toString(instance);
    }
}
