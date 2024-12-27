package io.nya.rpc.protocol.header;

import java.util.concurrent.atomic.AtomicLong;

public class IdFactory {
    private static final long MAX_ID = Long.MAX_VALUE;
    private static final AtomicLong counter = new AtomicLong(0);

    public static long getId() {
        long currentId = counter.incrementAndGet();
        if (currentId == MAX_ID) {
            // Reset the counter when it reaches the maximum value
            counter.set(1);
        }
        return currentId;
    }
}
