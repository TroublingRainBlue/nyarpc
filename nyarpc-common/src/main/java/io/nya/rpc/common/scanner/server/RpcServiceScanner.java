package io.nya.rpc.common.scanner.server;

import io.nya.rpc.annotation.RpcService;
import io.nya.rpc.common.helper.RpcServiceHelper;
import io.nya.rpc.common.scanner.ClassScanner;
import io.nya.rpc.protocol.meta.ServiceMetaData;
import io.nya.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nya
 * @version 1.0.0
 * 服务提供者注解扫描器
 */
public class RpcServiceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    /**
     * 扫描指定包下的类，并筛选出@RpcService标注的类
     */
    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(String scanPackage, String host, int port, RegistryService registryService) throws Exception {
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);

        if(classNameList.isEmpty()) {
            return handlerMap;
        }

        for (String className : classNameList) {
            LOGGER.info(className);
            try {
                Class<?> clazz = Class.forName(className);
                RpcService rpcService = clazz.getAnnotation(RpcService.class);
                if (rpcService != null) {
                    // 优先使用interfaceClass,interfaceClass的name为空，再使用interfaceClassName
                    String serviceName = getServiceName(rpcService);
                    // 向注册中心注册服务元数据
                    ServiceMetaData serviceMetaData = new ServiceMetaData(serviceName, rpcService.version(), host, port, rpcService.group());
                    registryService.registry(serviceMetaData);
                    String key = RpcServiceHelper.buildStringKey(serviceName,rpcService.version(), rpcService.group());
                    handlerMap.put(key, clazz.newInstance());
                }
            } catch (Exception e) {
                LOGGER.error("Scan classes throws exception===>>{}", e.toString());
            }
        }
        return handlerMap;
    }

    private static String getServiceName(RpcService rpcService){
        //优先使用interfaceClass
        Class clazz = rpcService.interfaceClass();
        if (clazz == null || clazz == void.class){
            return rpcService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName == null || serviceName.trim().isEmpty()){
            serviceName = rpcService.interfaceClassName();
        }
        return serviceName;
    }
}
