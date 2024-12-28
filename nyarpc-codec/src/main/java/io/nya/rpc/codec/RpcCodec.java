package io.nya.rpc.codec;

import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.serialization.jdk.JdkSerialization;

public interface RpcCodec {
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
