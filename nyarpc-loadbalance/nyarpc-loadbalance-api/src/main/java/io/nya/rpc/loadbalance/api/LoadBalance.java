package io.nya.rpc.loadbalance.api;

import io.nya.rpc.spi.annotation.SPI;

import java.util.List;

@SPI
public interface LoadBalance<T> {
    T select(List<T> serviceList, int hashcode);
}
