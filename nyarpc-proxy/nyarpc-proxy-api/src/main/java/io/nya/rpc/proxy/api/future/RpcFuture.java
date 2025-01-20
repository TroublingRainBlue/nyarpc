package io.nya.rpc.proxy.api.future;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import io.nya.rpc.proxy.api.callback.AsyncRpcCallback;
import io.nya.rpc.threadpool.ClientThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

public class RpcFuture extends CompletableFuture<Object> {
    private static final Logger logger = LoggerFactory.getLogger(RpcFuture.class);
    private Sync sync;
    private RpcProtocol<RpcRequest> request;
    private RpcProtocol<RpcResponse> response;
    private long startTime;
    private long responseTimeThreshold = 5000;
    private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    public RpcFuture(RpcProtocol<RpcRequest> request) {
        this.request = request;
        this.sync = new Sync();
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if(this.response != null) {
                return response.getBody().getResult();
        }
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if(success) {
            if(this.response != null) {
                return response.getBody().getResult();
            }
            return null;
        } else {
            throw new RuntimeException("Timeout exception. RequesrId:" + this.request.getHeader().getRequestId()
            + ". Request class name:" +
            this.request.getBody().getClassName()
            + ". Request method:" +
            this.request.getBody().getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw  new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcProtocol<RpcResponse> response) {
        this.response = response;
        sync.release(1);
        // 执行回调
        invokeCallbacks();

        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if(responseTime > responseTimeThreshold) {
            logger.warn("Service response too slowly. RequestId:{}. Response Time:{} ms", response.getHeader().getRequestId(), responseTime);
        }
    }

    // 回调方法
    private void runCallback(final AsyncRpcCallback callback) {
        final RpcResponse res = this.response.getBody();

        ClientThreadPool.submit(()->{
            if(res.getError() == null) {
                callback.onSuccess(res.getResult());
            } else {
                callback.onException(new RuntimeException("Response error:", new Throwable(res.getError())));
            }
        });
    }

    // 添加需要回调的接口
    public RpcFuture addCallback(AsyncRpcCallback callback) {
        lock.lock();
        try {
            if(isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    // 依次执行回调接口
    public void invokeCallbacks() {
        lock.lock();
        try {
            for(final AsyncRpcCallback callback : this.pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        // future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquire) {
            return getState() == done;
        }

        protected boolean tryRelease(int arg) {
            if(getState() == pending) {
                return compareAndSetState(pending, done);
            }
            return false;
        }

        public boolean isDone() {
            return getState() == done;
        }
    }
}
