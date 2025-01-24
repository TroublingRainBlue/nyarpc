package io.nya.rpc.common.scanner.reference;

import io.nya.rpc.annotation.RpcReference;
import io.nya.rpc.annotation.RpcService;
import io.nya.rpc.common.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author nya
 * @version 1.0.0
 */
public class RpcReferenceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceScanner.class);

    /**
     * 扫描指定包下的类，并筛选出@RpcService标注的类
     */
    public static Map<String, Object> doScannerWithRpcReferenceFilter(String scanPackage) throws Exception {

        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if(classNameList.isEmpty()) {
            return handlerMap;
        }

        for (String className : classNameList) {
            try {
                Class<?> clazz = Class.forName(className);
                Field[] fields = clazz.getDeclaredFields();
                Stream.of(fields).forEach(field -> {
                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    if (rpcReference != null) {
                        LOGGER.info("当前标注了@RpcReference注解的类实例名称===>>>{}", clazz.getName());
                        LOGGER.info("@RpcReference注解上标注的信息如下：");
                        LOGGER.info("registryType===>>>{}", rpcReference.registryType());
                        LOGGER.info("registryAddress===>>>{}", rpcReference.registryAddress());
                        LOGGER.info("loadBalanceType===>>>{}", rpcReference.loadBalanceType());
                        LOGGER.info("async===>>>{}", rpcReference.async());
                        LOGGER.info("oneway===>>>{}", rpcReference.oneway());
                        LOGGER.info("proxy===>>>{}", rpcReference.proxy());
                        LOGGER.info("version===>>>{}", rpcReference.version());
                        LOGGER.info("serializationType===>>>{}", rpcReference.serializationType());
                        LOGGER.info("timeout===>>>{}", rpcReference.timeout());
                        LOGGER.info("group===>>>{}", rpcReference.group());
                    }
                });

            } catch (Exception e) {
                LOGGER.error("Scan classes throws exception===>>{}", e.toString());
            }
        }
        return handlerMap;
    }
}
