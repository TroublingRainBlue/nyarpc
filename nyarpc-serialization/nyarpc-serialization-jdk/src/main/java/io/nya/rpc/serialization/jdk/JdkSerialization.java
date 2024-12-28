package io.nya.rpc.serialization.jdk;

import io.nya.rpc.common.exception.SerializationException;
import io.nya.rpc.serialization.api.Serialization;

import java.io.*;

public class JdkSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) {
        if(obj == null) {
            throw new SerializationException("Serialized obj is null");
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e, e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        if(data == null) {
            throw new SerializationException("Deserialized data is null ");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializationException(e, e.getMessage());
        }

    }
}
