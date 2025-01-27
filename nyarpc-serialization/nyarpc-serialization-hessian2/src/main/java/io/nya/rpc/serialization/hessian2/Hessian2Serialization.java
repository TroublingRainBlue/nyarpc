package io.nya.rpc.serialization.hessian2;


import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import io.nya.rpc.common.exception.SerializationException;
import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SPIClass
public class Hessian2Serialization implements Serialization {
    private static final Logger logger = LoggerFactory.getLogger(Hessian2Serialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        logger.info("execute Hessian2 serialize...");
        if(obj == null) {
            throw new SerializationException("Serialized obj is null");
        }
        byte[] data;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        try  {
            hessian2Output.startMessage(); // 开始消息
            hessian2Output.writeObject(obj);
            hessian2Output.completeMessage(); // 结束消息
            hessian2Output.flush();
            data =  byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e, e.getMessage());
        } finally {
            try {
                byteArrayOutputStream.close();
                hessian2Output.close();
            } catch (IOException e) {
                throw new SerializationException(e, e.getMessage());
            }
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.info("execute Hessian2 deserialize...");
        if(data == null) {
            throw new SerializationException("Deserialized data is null ");
        }
        T obj = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        try  {
            // 读取消息开始
            hessian2Input.startMessage();

            // 读取 Java 对象
            obj = (T) hessian2Input.readObject(cls);

            // 结束消息
            hessian2Input.completeMessage();

        } catch (IOException e) {
            throw new SerializationException(e, e.getMessage());
        } finally {
            try {
                byteArrayInputStream.close();
                hessian2Input.close();
            } catch (IOException e) {
                throw new SerializationException(e, e.getMessage());
            }
        }
        return obj;
    }
}
