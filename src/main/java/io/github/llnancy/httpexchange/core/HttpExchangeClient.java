package io.github.llnancy.httpexchange.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ClientCodecConfigurer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http exchange client annotation
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface HttpExchangeClient {

    /**
     * the base url string
     *
     * @return base url
     */
    String baseUrl() default "";

    /**
     * the default header key
     *
     * @return header key
     */
    String defaultHeaderKey() default "";

    /**
     * the default header values
     *
     * @return an array of header value
     */
    String[] defaultHeaderValues() default {};

    /**
     * the class which hold a {@link HttpHeaders}
     *
     * @return class of {@link HttpHeadersConsumer}
     */
    Class<? extends HttpHeadersConsumer> httpHeadersConsumer() default HttpHeadersConsumer.class;

    /**
     * the class which hold a {@link ClientCodecConfigurer}
     *
     * @return class of {@link ClientCodecConfigurerConsumer}
     */
    Class<? extends ClientCodecConfigurerConsumer> codecConfigurerConsumer() default ClientCodecConfigurerConsumer.class;
}
