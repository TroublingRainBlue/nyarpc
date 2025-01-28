package io.nya.rpc.serialization.protostuff;

import io.nya.rpc.common.exception.SerializationException;
import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.spi.annotation.SPIClass;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SPIClass
public class ProtostuffSerialization implements Serialization {
    private static final Logger logger = LoggerFactory.getLogger(ProtostuffSerialization.class);

    // 缓存Schema
    private static final ConcurrentMap<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    /**
     * 获取Schema，如果缓存中没有则创建并放入缓存
     */
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cachedSchema.computeIfAbsent(clazz, RuntimeSchema::getSchema);
    }

    @Override
    public <T> byte[] serialize(T obj) {
        logger.info("execute protostuff serialize...");
        if (obj == null){
            throw new SerializationException("serialize object is null");
        }
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.info("execute protostuff deserialize...");
        if (data == null){
            throw new SerializationException("deserialize data is null");
        }
        Schema<T> schema = getSchema(cls);
        T message = schema.newMessage();
        ProtobufIOUtil.mergeFrom(data, message, schema);
        return message;
    }
}
