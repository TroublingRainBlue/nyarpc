package io.nya.rpc.spi.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface SPI {
    String value() default "";
}
