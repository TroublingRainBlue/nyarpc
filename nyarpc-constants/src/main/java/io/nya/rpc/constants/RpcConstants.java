package io.nya.rpc.constants;

public class RpcConstants {
    // 魔数
    public static final short MAGIC = 1178;
    // Header长度
    public static final int HEADER_TOTAL_LEN = 32;
    // 通过反射进行方法调用
    public static final String REFLECT_TYPE_JDK = "jdk";
    // 通过CGLib动态代理进行方法调用
    public static final String REFLECT_TYPE_CGLib = "cglib";

    public static final String SERIALIZATION_JDK = "jdk";
}
