package io.github.llnancy.exchangeclient.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * exchange client annotation
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ExchangeClient {

    String baseUrl() default "";

    String defaultHeaderKey() default "";

    String[] defaultHeaderValues() default {};

    Class<? extends HttpHeadersConsumer> httpHeadersConsumer() default HttpHeadersConsumer.class;

    Class<? extends ClientCodecConfigurerConsumer> codecConfigurerConsumer() default ClientCodecConfigurerConsumer.class;
}
