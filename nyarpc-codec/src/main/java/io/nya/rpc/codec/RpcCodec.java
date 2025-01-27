package io.nya.rpc.codec;

import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.serialization.jdk.JdkSerialization;
import io.nya.rpc.spi.ExtensionLoader;

public interface RpcCodec {
    default Serialization getSerialization(String serializationType) {
        return ExtensionLoader.getExtension(Serialization.class, serializationType);
    }
}
