package io.nya.rpc.consumer.common.callback;

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
