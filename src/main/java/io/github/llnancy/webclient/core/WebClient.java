package io.github.llnancy.webclient.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web client annotation
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface WebClient {

    String baseUrl() default "";

    Class<? extends ClientCodecConfigurerConsumer> codecConfigurerConsumer() default ClientCodecConfigurerConsumer.class;
}
