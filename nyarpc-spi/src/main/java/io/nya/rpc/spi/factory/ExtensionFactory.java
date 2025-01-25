package io.nya.rpc.spi.factory;

import io.nya.rpc.spi.annotation.SPI;

@SPI("spi")
public interface ExtensionFactory {
    /**
     * 获取扩展类对象
     * @param key
     * @param clazz
     * @return
     * @param <T>
     */
    <T> T getExtension(String key, Class<T> clazz);
}
