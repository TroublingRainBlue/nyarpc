package io.nya.rpc.common.scanner.server;

import io.nya.rpc.annotation.RpcService;
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
            try {
                Class<?> clazz = Class.forName(className);
                RpcService rpcService = clazz.getAnnotation(RpcService.class);
                if (rpcService != null) {
                    // 优先使用interfaceClass,interfaceClass的name为空，再使用interfaceClassName
                    // TODO 向注册中心注册服务元数据，同时handlerMap标记RpcService注解标注的实例

                    LOGGER.info("当前标注了@RpcService注解的类实例名称===>>>{}", clazz.getName());
                    LOGGER.info("@RpcService注解上标注的信息如下：");
                    LOGGER.info("interfaceClass===>>>{}", rpcService.interfaceClass().getName());
                    LOGGER.info("interfaceClassName===>>>{}", rpcService.interfaceClassName());
                    LOGGER.info("version===>>>{}", rpcService.version());
                    LOGGER.info("group===>>>{}", rpcService.group());
                }
            } catch (Exception e) {
                LOGGER.error("Scan classes throws exception===>>{}", e.toString());
            }
        }
        return handlerMap;
    }
}
