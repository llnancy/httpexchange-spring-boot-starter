package io.github.llnancy.httpexchange.core;

import io.github.llnancy.httpexchange.util.ApplicationContextUtils;
import io.github.llnancy.httpexchange.util.HttpExchangeClientUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;

/**
 * http exchange client factory bean
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@SuppressWarnings("NullableProblems")
public class HttpExchangeClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware, ApplicationContextAware {

    private Class<T> httpExchangeClientInterface;

    private Environment environment;

    private ApplicationContext applicationContext;

    public HttpExchangeClientFactoryBean() {
    }

    public HttpExchangeClientFactoryBean(Class<T> httpExchangeClientInterface) {
        this.httpExchangeClientInterface = httpExchangeClientInterface;
    }

    public void setHttpExchangeClientInterface(Class<T> httpExchangeClientInterface) {
        this.httpExchangeClientInterface = httpExchangeClientInterface;
    }

    @Override
    public T getObject() {
        return createHttpServiceProxyFactory().createClient(httpExchangeClientInterface);
    }

    private HttpServiceProxyFactory createHttpServiceProxyFactory() {
        return HttpServiceProxyFactory.builder(createWebClientAdapter()).build();
    }

    private WebClientAdapter createWebClientAdapter() {
        return WebClientAdapter.forClient(createWebClient());
    }

    private WebClient createWebClient() {
        HttpExchangeClient httpExchangeClient = AnnotatedElementUtils.findMergedAnnotation(httpExchangeClientInterface, HttpExchangeClient.class);
        String baseUrl = HttpExchangeClientUtils.convertBaseUrl(Objects.requireNonNull(httpExchangeClient).baseUrl(), environment);
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, ClientResponse::createException);
        configureExchangeStrategies(httpExchangeClient, builder);
        configureDefaultHeader(httpExchangeClient, builder);
        configureDefaultHeaders(httpExchangeClient, builder);
        return builder.build();
    }

    private void configureExchangeStrategies(HttpExchangeClient httpExchangeClient, WebClient.Builder builder) {
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
    }

    private void configureDefaultHeader(HttpExchangeClient httpExchangeClient, WebClient.Builder builder) {
        String headerKey = httpExchangeClient.defaultHeaderKey();
        String[] headerValues = httpExchangeClient.defaultHeaderValues();
        if (StringUtils.hasText(headerKey) && Objects.nonNull(headerValues) && headerValues.length != 0) {
            builder.defaultHeader(headerKey, headerValues);
        }
    }

    private void configureDefaultHeaders(HttpExchangeClient httpExchangeClient, WebClient.Builder builder) {
        Class<? extends HttpHeadersConsumer> clazz = httpExchangeClient.httpHeadersConsumer();
        HttpHeadersConsumer consumer = null;
        if (clazz != HttpHeadersConsumer.class) {
            consumer = ApplicationContextUtils.getBeanOrReflect(applicationContext, clazz);
        }
        if (Objects.nonNull(consumer)) {
            builder.defaultHeaders(consumer.consumer());
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
