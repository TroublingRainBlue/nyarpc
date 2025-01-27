package io.nya.rpc.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.nya.rpc.common.exception.SerializationException;
import io.nya.rpc.serialization.api.Serialization;
import io.nya.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

@SPIClass
public class JSONSerialization implements Serialization {
    private final Logger logger = LoggerFactory.getLogger(JSONSerialization.class);
    private static ObjectMapper objMapper = new ObjectMapper();
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objMapper.setDateFormat(dateFormat);
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        objMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        objMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
    }
    @Override
    public <T> byte[] serialize(T obj) {
        logger.info("execute JSON serialize...");
        if(obj == null) {
            throw new SerializationException("Serialized obj is null");
        }
        byte[] data = null;
        try {
            data = objMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e, e.getMessage());
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.info("execute JSON deserialize...");
        if(data == null || data.length == 0) {
            throw new SerializationException("Deserialized data is null");
        }
        T obj = null;
        try {
            obj = objMapper.readValue(data, cls);
        } catch (IOException e) {
            throw new SerializationException(e, e.getMessage());
        }
        return obj;
    }
}
