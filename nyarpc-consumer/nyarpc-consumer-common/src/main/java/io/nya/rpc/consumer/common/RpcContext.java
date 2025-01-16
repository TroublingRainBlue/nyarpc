package io.nya.rpc.consumer.common;

public class RpcContext {
    private static RpcContext instance = new RpcContext();
    private RpcContext() {}

    /**
     * 获取RPC服务的上下文
     */
    public static RpcContext getInstance() {
        return instance;
    }

    /**
     * RpcFuture的InheritableThreadLocal
     */
    private static final InheritableThreadLocal<RpcFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();


    /**
     * 将RpcFuture保存到线程上下文中
     */
    public void setRpcFuture(RpcFuture rpcFuture) {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
    }

    /**
     * 获取RpcFuture
     */
    public RpcFuture getRpcFuture() {
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * 移除RpcFuture
     */
    public void removeRpcFuture() {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }
}
