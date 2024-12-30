package io.nya.rpc.common.helper;

public class RpcServiceHelper {
    public static String buildStringKey(String serviceName, String version, String group) {
        return String.join("#", serviceName, version, group);
    }
}
