package io.nya.rpc.consumer.common;

import io.nya.rpc.protocol.RpcProtocol;
import io.nya.rpc.protocol.request.RpcRequest;
import io.nya.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class RpcFuture extends CompletableFuture<Object> {
    private static final Logger logger = LoggerFactory.getLogger(RpcFuture.class);
    private Sync sync;
    private RpcProtocol<RpcRequest> request;
    private RpcProtocol<RpcResponse> response;
    private long startTime;
    private long responseTimeThreshold = 5000;

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
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;

        if(responseTime > responseTimeThreshold) {
            logger.warn("Service response too slowly. RequestId:{}. Response Time:{} ms", response.getHeader().getRequestId(), responseTime);
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
