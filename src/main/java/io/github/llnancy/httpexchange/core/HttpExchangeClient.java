package io.github.llnancy.httpexchange.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

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
     * {@link WebClient.Builder#defaultUriVariables(Map)} supplier
     *
     * @return class of {@link DefaultUriVariablesSupplier}
     */
    Class<? extends DefaultUriVariablesSupplier> defaultUriVariablesSupplier() default DefaultUriVariablesSupplier.class;

    /**
     * {@link WebClient.Builder#uriBuilderFactory(UriBuilderFactory)}
     *
     * @return class of {@link UriBuilderFactory}
     */
    Class<? extends UriBuilderFactory> uriBuilderFactory() default UriBuilderFactory.class;

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
     * the default cookie key
     *
     * @return cookie key
     */
    String defaultCookieKey() default "";

    /**
     * the default cookie values
     *
     * @return an array of cookie value
     */
    String[] defaultCookieValues() default {};

    /**
     * the class which hold a cookie map
     *
     * @return class of {@link CookiesConsumer}
     */
    Class<? extends CookiesConsumer> cookiesConsumer() default CookiesConsumer.class;

    /**
     * the class which hold a {@link RequestHeadersSpecConsumer}
     *
     * @return class of {@link RequestHeadersSpecConsumer}
     */
    Class<? extends RequestHeadersSpecConsumer> requestHeadersSpecConsumer() default RequestHeadersSpecConsumer.class;

    /**
     * the class which hold a {@link DefaultStatusHandlerHolder}
     *
     * @return class of {@link DefaultStatusHandlerHolder}
     */
    Class<? extends DefaultStatusHandlerHolder> defaultStatusHandlerHolder() default DefaultStatusHandlerHolder.class;

    /**
     * filter exchange
     *
     * @return class of {@link ExchangeFilterFunction}
     */
    Class<? extends ExchangeFilterFunction> filter() default ExchangeFilterFunction.class;

    /**
     * the class which hold list of {@link ExchangeFilterFunctionsConsumer}
     *
     * @return class of {@link ExchangeFilterFunctionsConsumer}
     */
    Class<? extends ExchangeFilterFunctionsConsumer> filtersConsumer() default ExchangeFilterFunctionsConsumer.class;

    /**
     * the class which hold a {@link ClientCodecConfigurer}
     *
     * @return class of {@link ClientCodecConfigurerConsumer}
     */
    Class<? extends ClientCodecConfigurerConsumer> codecConfigurerConsumer() default ClientCodecConfigurerConsumer.class;

}
