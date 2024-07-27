package io.github.llnancy.httpexchange.core;

import io.github.llnancy.httpexchange.util.ApplicationContextUtils;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriBuilderFactory;

import java.util.Objects;

/**
 * http exchange client factory bean
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@SuppressWarnings("NullableProblems")
public class HttpExchangeClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware, ApplicationContextAware {

    /**
     * the interface class annotated by {@link HttpExchangeClient}
     */
    @Setter
    private Class<T> httpExchangeClientInterface;

    private Environment environment;

    private ApplicationContext applicationContext;

    /**
     * default constructor
     */
    public HttpExchangeClientFactoryBean() {
    }

    /**
     * constructor with httpExchangeClientInterface
     *
     * @param httpExchangeClientInterface the interface class annotated by {@link HttpExchangeClient}
     */
    public HttpExchangeClientFactoryBean(Class<T> httpExchangeClientInterface) {
        this.httpExchangeClientInterface = httpExchangeClientInterface;
    }

    @Override
    public T getObject() {
        return createHttpServiceProxyFactory().createClient(httpExchangeClientInterface);
    }

    private HttpServiceProxyFactory createHttpServiceProxyFactory() {
        return HttpServiceProxyFactory.builderFor(createWebClientAdapter()).build();
    }

    private WebClientAdapter createWebClientAdapter() {
        return WebClientAdapter.create(createWebClient());
    }

    private WebClient createWebClient() {
        HttpExchangeClient httpExchangeClient = AnnotatedElementUtils.findMergedAnnotation(httpExchangeClientInterface, HttpExchangeClient.class);
        WebClientConfigure configure = new WebClientConfigure(httpExchangeClient, WebClient.builder(), applicationContext, environment);
        return configure.baseUrl()
                .defaultUriVariables()
                .uriBuilderFactory()
                .defaultHeader()
                .defaultHeaders()
                .defaultCookie()
                .defaultCookies()
                .requestHeadersSpec()
                .filter()
                .filters()
                .defaultStatusHandler()
                .exchangeStrategies()
                .build();
    }

    private record WebClientConfigure(HttpExchangeClient httpExchangeClient,
                                      WebClient.Builder builder,
                                      ApplicationContext applicationContext,
                                      Environment environment) {

        /**
         * convert base url
         *
         * @param baseUrl     source baseUrl
         * @param environment {@link Environment}
         * @return target baseUrl
         */
        private static String convertBaseUrl(String baseUrl, Environment environment) {
            if (StringUtils.hasText(baseUrl)) {
                baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
                // 解析 baseUrl 占位符
                if (!baseUrl.endsWith("/")) {
                    baseUrl += "/";
                }
            }
            return baseUrl;
        }

        public WebClientConfigure baseUrl() {
            builder.baseUrl(convertBaseUrl(httpExchangeClient.baseUrl(), environment));
            return this;
        }

        public WebClientConfigure defaultUriVariables() {
            Class<? extends DefaultUriVariablesSupplier> clazz = httpExchangeClient.defaultUriVariablesSupplier();
            DefaultUriVariablesSupplier supplier = null;
            if (clazz != DefaultUriVariablesSupplier.class) {
                supplier = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(supplier)) {
                builder.defaultUriVariables(supplier.supply());
            }
            return this;
        }

        public WebClientConfigure uriBuilderFactory() {
            Class<? extends UriBuilderFactory> clazz = httpExchangeClient.uriBuilderFactory();
            UriBuilderFactory uriBuilderFactory = null;
            if (clazz != UriBuilderFactory.class) {
                uriBuilderFactory = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(uriBuilderFactory)) {
                builder.uriBuilderFactory(uriBuilderFactory);
            }
            return this;
        }

        public WebClientConfigure filter() {
            Class<? extends ExchangeFilterFunction> clazz = httpExchangeClient.filter();
            ExchangeFilterFunction filterFunction = null;
            if (clazz != ExchangeFilterFunction.class) {
                filterFunction = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(filterFunction)) {
                builder.filter(filterFunction);
            }
            return this;
        }

        public WebClientConfigure filters() {
            Class<? extends ExchangeFilterFunctionsConsumer> clazz = httpExchangeClient.filtersConsumer();
            ExchangeFilterFunctionsConsumer functionsConsumer = null;
            if (clazz != ExchangeFilterFunctionsConsumer.class) {
                functionsConsumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(functionsConsumer)) {
                builder.filters(functionsConsumer.consume());
            }
            return this;
        }

        public WebClientConfigure defaultHeader() {
            String headerKey = httpExchangeClient.defaultHeaderKey();
            String[] headerValues = httpExchangeClient.defaultHeaderValues();
            if (StringUtils.hasText(headerKey) && Objects.nonNull(headerValues) && headerValues.length != 0) {
                builder.defaultHeader(headerKey, headerValues);
            }
            return this;
        }

        public WebClientConfigure defaultHeaders() {
            Class<? extends HttpHeadersConsumer> clazz = httpExchangeClient.httpHeadersConsumer();
            HttpHeadersConsumer consumer = null;
            if (clazz != HttpHeadersConsumer.class) {
                consumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(consumer)) {
                builder.defaultHeaders(consumer.consume());
            }
            return this;
        }

        public WebClientConfigure defaultCookie() {
            String cookieKey = httpExchangeClient.defaultCookieKey();
            String[] cookieValues = httpExchangeClient.defaultCookieValues();
            if (StringUtils.hasText(cookieKey) && Objects.nonNull(cookieValues) && cookieValues.length != 0) {
                builder.defaultCookie(cookieKey, cookieValues);
            }
            return this;
        }

        public WebClientConfigure defaultCookies() {
            Class<? extends CookiesConsumer> clazz = httpExchangeClient.cookiesConsumer();
            CookiesConsumer consumer = null;
            if (clazz != CookiesConsumer.class) {
                consumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(consumer)) {
                builder.defaultCookies(consumer.consume());
            }
            return this;
        }

        public WebClientConfigure requestHeadersSpec() {
            Class<? extends RequestHeadersSpecConsumer> clazz = httpExchangeClient.requestHeadersSpecConsumer();
            RequestHeadersSpecConsumer consumer = null;
            if (clazz != RequestHeadersSpecConsumer.class) {
                consumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(consumer)) {
                builder.defaultRequest(consumer.consume());
            }
            return this;
        }

        public WebClientConfigure defaultStatusHandler() {
            Class<? extends DefaultStatusHandlerHolder> clazz = httpExchangeClient.defaultStatusHandlerHolder();
            DefaultStatusHandlerHolder defaultStatusHandlerHolder = null;
            if (clazz != DefaultStatusHandlerHolder.class) {
                defaultStatusHandlerHolder = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(defaultStatusHandlerHolder)) {
                builder.defaultStatusHandler(defaultStatusHandlerHolder.statusPredicate(), defaultStatusHandlerHolder.exceptionFunction());
            }
            return this;
        }

        public WebClientConfigure exchangeStrategies() {
            Class<? extends ClientCodecConfigurerConsumer> clazz = httpExchangeClient.codecConfigurerConsumer();
            ClientCodecConfigurerConsumer consumer = null;
            if (clazz != ClientCodecConfigurerConsumer.class) {
                consumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
            }
            if (Objects.nonNull(consumer)) {
                builder.exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(consumer.consumer())
                                .build()
                );
            }
            return this;
        }

        public WebClient build() {
            return builder.build();
        }
    }

    @Override
    public Class<?> getObjectType() {
        return this.httpExchangeClientInterface;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
