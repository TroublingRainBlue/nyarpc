package io.nya.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nya
 * @version 1.0.0
 * 服务消费者注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {
    /**
     * 版本号
     */
    String version() default "1.0.0";

    /**
     * 注册中心类型：如zookeeper、nacos、etcd、consul
     */
    String registryType() default "zookeeper";

    /**
     * 注册中心地址，默认本地
     */
    String registryAddress() default "127.0.0.1";

    /**
     * 负载均衡类型,默认zk的一致性Hash
     */
    String loadBalanceType() default "zkconsistenhash";

    /**
     * 序列化类型：protostuff、kryo、json、jdk、fst、hessian2
     */
    String serializationType() default "protostuff";

    /**
     * 超时时间，默认5s
     */
    long timeout() default 5000;

    /**
     * 是否异步执行
     */
    boolean async() default false;

    /**
     * 是否单向调用
     */
    boolean oneway() default false;

    /**
     * 代理类型
     */
    String proxy() default "jdk";

    /**
     * 服务分组，默认为空
     */
    String group() default "";
}
