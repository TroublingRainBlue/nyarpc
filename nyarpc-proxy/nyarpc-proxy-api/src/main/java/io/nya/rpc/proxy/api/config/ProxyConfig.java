package io.nya.rpc.proxy.api.config;

import io.nya.rpc.proxy.api.consumer.Consumer;

import java.io.Serializable;

public class ProxyConfig<T> implements Serializable {
    private static final long serialVersionUID = 6648940252795742398L;

    /**
     * 接口的Class对象
     */
    private Class<T> clazz;

    /**
     * 服务版本号
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 超时时间
     */
    private long timeout = 15000;

    /**
     * 服务消费者
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public ProxyConfig(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.async = async;
        this.serializationType = serializationType;
        this.oneway = oneway;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public boolean getAsync() {
        return async;
    }

    public boolean getOneway() {
        return oneway;
    }
}
