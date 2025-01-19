package io.nya.rpc.proxy.api.callback;

public interface AsyncRpcCallback {
    /**
     * 成功的回调方法
     */
    void onSuccess(Object result);

    /**
     * 异常的回调方法
     */
    void onException(Exception e);
}
