package io.nya.rpc.protocol.meta;

import java.io.Serializable;

/**
 * 服务元数据，注册中心与提供者消费者交换的信息
 */
public class ServiceMetaData implements Serializable {
    private final static long serialVersionUID=6289735590272020366L;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion;

    /**
     * 服务地址
     */
    private String serviceAddr;

    /**
     * 服务端口号
     */
    private int servicePort;

    /**
     * 服务分组
     */
    private String serviceGroup;

    public ServiceMetaData(){}

    public ServiceMetaData(String serviceName, String serviceVersion, String serviceAddr, int servicePort, String serviceGroup) {
        this.serviceGroup = serviceGroup;
        this.servicePort = servicePort;
        this.serviceAddr = serviceAddr;
        this.serviceVersion = serviceVersion;
        this.serviceName = serviceName;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setServiceAddr(String serviceAddr) {
        this.serviceAddr = serviceAddr;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getServiceAddr() {
        return serviceAddr;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getServicePort() {
        return servicePort;
    }
}
