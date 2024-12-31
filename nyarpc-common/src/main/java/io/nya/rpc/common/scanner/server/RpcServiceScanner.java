package io.nya.rpc.common.scanner.server;

import io.nya.rpc.annotation.RpcService;
import io.nya.rpc.common.helper.RpcServiceHelper;
import io.nya.rpc.common.scanner.ClassScanner;
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
    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(String scanPackage) throws Exception {
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
                    // 向注册中心注册服务元数据，同时handlerMap标记RpcService注解标注的实例
                    String serviceName = getServiceName(rpcService);
                    String key = RpcServiceHelper.buildStringKey(serviceName,rpcService.version(), rpcService.group());
                    handlerMap.put(key, clazz.newInstance());

                }
            } catch (Exception e) {
                LOGGER.error("Scan classes throws exception===>>{}", e.toString());
            }
        }
        return handlerMap;
    }

    private static String getServiceName(RpcService rpcService) {
        // if(rpcService.interfaceClass() != null)return rpcService.interfaceClass().toString();
        return rpcService.interfaceClassName();
    }
}
