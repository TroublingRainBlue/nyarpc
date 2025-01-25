package io.nya.rpc.loadbalance.api;

import java.util.List;

public interface LoadBalance<T> {
    T select(List<T> serviceList, int hashcode);
}
