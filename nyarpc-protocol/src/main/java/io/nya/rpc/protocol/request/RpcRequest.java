package io.nya.rpc.protocol.request;

import io.nya.rpc.protocol.base.RpcMessage;

public class RpcRequest extends RpcMessage {
    private static final long serialVersionUID = 5555776886650396129L;
    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型数组
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数数组
     */
    private Object[] params;

    /**
     * 版本号
     */
    private String version;

    /**
     * 分组
     */
    private String group;

    // getter、setter

    public void setClassName(String className) {
        this.className = className;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    public Object[] getParams() {
        return this.params;
    }

    public String getClassName() {
        return this.className;
    }

    public String getGroup() {
        return this.group;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getVersion() {
        return this.version;
    }
}
