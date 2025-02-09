package io.nya.rpc.loadbalance.api;

import io.nya.rpc.constants.RpcConstants;
import io.nya.rpc.spi.annotation.SPI;

import java.util.List;

@SPI(RpcConstants.LOADBALANCE_ROBIN)
public interface LoadBalance<T> {
    T select(List<T> serviceList, int hashcode, String ip);
}
