package io.nya.rpc.registry.api.config;

import java.io.Serializable;

public class RegistryConfig implements Serializable {
    private static final long serialVersionUID = -7248658103788758893L;

    /**
     * 注册中心地址
     */
    private String registryAddress;

    /**
     * 注册类型
     */
    private String registryType;

    /**
     * 负载均衡类型
     */
    private String loadBalance;

    public RegistryConfig(String registryAddress, String registryType, String loadBalance) {
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.loadBalance = loadBalance;
    }

    public void setRegistryAddr(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public String getRegistryAddr() {
        return registryAddress;
    }

    public String getRegistryType() {
        return registryType;
    }

    public String getLoadBalance() {
        return loadBalance;
    }
}
