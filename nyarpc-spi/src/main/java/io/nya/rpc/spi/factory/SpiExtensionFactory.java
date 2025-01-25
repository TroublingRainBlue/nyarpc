package io.nya.rpc.spi.factory;

import io.nya.rpc.spi.ExtensionLoader;
import io.nya.rpc.spi.annotation.SPI;
import io.nya.rpc.spi.annotation.SPIClass;

import java.util.Optional;

@SPIClass
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                .orElse(null);
    }
}

