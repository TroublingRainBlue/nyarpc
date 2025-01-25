package io.nya.rpc.registry.api;

import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.registry.api.config.RegistryConfig;

public interface RegistryService {
    /**
     * 服务注册
     * @param serviceMetaData 服务元数据
     * @exception Exception 抛出异常
     */
    void registry(ServiceMetaData serviceMetaData) throws Exception;

    /**
     * 服务注销
     * @param serviceMetaData 服务元数据
     * @throws Exception 抛出异常
     */
    void unRegistry(ServiceMetaData serviceMetaData) throws Exception;

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @param invokeHashCode 负载均衡相关
     * @return 服务元数据
     * @throws Exception 抛出异常
     */
    ServiceMetaData discover(String serviceName, int invokeHashCode) throws Exception;

    /**
     * 服务销毁
     * @throws Exception 抛出异常
     */
    void destroy() throws Exception;

    /**
     * 初始化方法
     * @param config 配置信息
     * @throws Exception 抛出异常
     */
    default void init(RegistryConfig config) throws Exception{

    }
}
