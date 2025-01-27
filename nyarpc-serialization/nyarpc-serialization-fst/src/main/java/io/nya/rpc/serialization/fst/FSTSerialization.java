package io.nya.rpc.serialization.fst;

import io.nya.rpc.common.exception.SerializationException;
import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.spi.annotation.SPIClass;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@SPIClass
public class FSTSerialization implements Serialization {
    private static final Logger logger = LoggerFactory.getLogger(FSTSerialization.class);
    // 创建并配置FSTConfiguration实例
    private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    static {
        // 注册常用类，提升序列化效率
        conf.registerClass(String.class, ArrayList.class /* add other classes you frequently serialize */);
    }

    @Override
    public <T> byte[] serialize(T obj) {
        logger.info("execute FST serialize...");
        if(obj == null) {
            throw new SerializationException("Serialized obj is null");
        }
        byte[] data;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();FSTObjectOutput fout = conf.getObjectOutput(out)) {
            fout.writeObject(obj);
            fout.flush();
            data = out.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e, e.getMessage());
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.info("execute FST deserialize...");
        if(data == null) {
            throw new SerializationException("Deserialized data is null ");
        }
        T obj = null;
        try (ByteArrayInputStream in = new ByteArrayInputStream(data);FSTObjectInput fstIn = conf.getObjectInput(in)) {
            obj = (T) fstIn.readObject();
        } catch (Exception e) {
            throw new SerializationException(e, e.getMessage());
        }
        return obj;
    }
}
