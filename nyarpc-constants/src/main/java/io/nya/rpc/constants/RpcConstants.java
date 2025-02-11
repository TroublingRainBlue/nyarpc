package io.nya.rpc.constants;

public class RpcConstants {
    // 魔数
    public static final short MAGIC = 0x1234;
    // Header长度
    public static final int HEADER_TOTAL_LEN = 32;
    // 通过反射进行方法调用
    public static final String REFLECT_TYPE_JDK = "jdk";
    // 通过CGLib动态代理进行方法调用
    public static final String REFLECT_TYPE_CGLib = "cglib";
    // 通过javassist进行方法调用
    public static final String REFLECT_TYPE_JAVASSIST = "javassist";
    // protostuff 序列化
    public static final String SERIALIZATION_PROTOSTUFF = "protostuff";
    // FST 序列化
    public static final String SERIALIZATION_FST = "fst";
    // hessian2 序列化
    public static final String SERIALIZATION_HESSIAN2 = "hessian2";
    // jdk 序列化
    public static final String SERIALIZATION_JDK = "jdk";
    // json 序列化
    public static final String SERIALIZATION_JSON = "json";
    // kryo 序列化
    public static final String SERIALIZATION_KRYO = "kryo";
    // jdk 动态代理生成请求对象
    public static final String PROXY_JDK = "jdk";
    // cglib 动态代理生成请求对象
    public static final String PROXY_CGLIB = "cglib";
    // javassist 动态代理生成请求对象
    public static final String PROXY_JAVASSIST = "javassist";
    // 随机负载均衡策略
    public static final String LOADBALANCE_RANDOM = "random";
    // 加权随机负载均衡策略
    public static final String LOADBALANCE_RANDOM_WEIGHT = "randomweight";
    // 轮询负载均衡策略
    public static final String LOADBALANCE_ROBIN = "robin";
    // 加权轮询负载均衡策略
    public static final String LOADBALANCE_ROBIN_WEIGHT = "robinweight";
    // hash 负载均衡策略
    public static final String LOADBALANCE_HASH = "hash";
    // 加权hash 负载均衡策略
    public static final String LOADBALANCE_HASH_WEIGHT = "hashweight";
    // Ip Hash 负载均衡策略
    public static final String LOADBALANCE_IP_HASH = "iphash";
    // 基于zookeeper的一致性Hash 负载均衡策略
    public static final String LOADBALANCE_ZK_CONSISTEN_HASH = "zkconsistenhash";
    // 基于增强型加权随机负载均衡策略
    public static final String LOADBALANCE_RANDOM_WEIGHT_ENHANCE = "randomweightenhance";
    // 服务权重最大值
    public static final int MAX_WEIGHT = 100;
    // 服务权重最小值
    public static final int MIN_WEIGHT = 0;
}
