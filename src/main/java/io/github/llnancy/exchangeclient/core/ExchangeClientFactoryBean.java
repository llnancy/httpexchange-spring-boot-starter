package io.github.llnancy.exchangeclient.core;

import io.github.llnancy.exchangeclient.util.ApplicationContextUtils;
import io.github.llnancy.exchangeclient.util.ExchangeClientUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;

/**
 * exchange client factory bean
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
public class ExchangeClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware, ApplicationContextAware {

    private final Class<T> exchangeClientInterface;

    private Environment environment;

    private ApplicationContext applicationContext;

    public ExchangeClientFactoryBean(Class<T> exchangeClientInterface) {
        this.exchangeClientInterface = exchangeClientInterface;
    }

    @Override
    public T getObject() throws Exception {
        return createHttpServiceProxyFactory().createClient(exchangeClientInterface);
    }

    private HttpServiceProxyFactory createHttpServiceProxyFactory() {
        return HttpServiceProxyFactory.builder(createWebClientAdapter()).build();
    }

    private WebClientAdapter createWebClientAdapter() {
        return WebClientAdapter.forClient(createWebClient());
    }

    private WebClient createWebClient() {
        ExchangeClient exchangeClient =
                AnnotatedElementUtils.findMergedAnnotation(exchangeClientInterface, ExchangeClient.class);
        String baseUrl = ExchangeClientUtils.convertBaseUrl(Objects.requireNonNull(exchangeClient).baseUrl(), environment);
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, ClientResponse::createException);
        configureExchangeStrategies(exchangeClient, builder);
        configureDefaultHeader(exchangeClient, builder);
        configureDefaultHeaders(exchangeClient, builder);
        return builder.build();
    }

    private void configureExchangeStrategies(ExchangeClient exchangeClient, WebClient.Builder builder) {
        Class<? extends ClientCodecConfigurerConsumer> clazz = exchangeClient.codecConfigurerConsumer();
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

    private void configureDefaultHeader(ExchangeClient exchangeClient, WebClient.Builder builder) {
        String headerKey = exchangeClient.defaultHeaderKey();
        String[] headerValues = exchangeClient.defaultHeaderValues();
        if (StringUtils.hasText(headerKey) && Objects.nonNull(headerValues) && headerValues.length != 0) {
            builder.defaultHeader(headerKey, headerValues);
        }
    }

    private void configureDefaultHeaders(ExchangeClient exchangeClient, WebClient.Builder builder) {
        Class<? extends HttpHeadersConsumer> clazz = exchangeClient.httpHeadersConsumer();
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
        return this.exchangeClientInterface;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
