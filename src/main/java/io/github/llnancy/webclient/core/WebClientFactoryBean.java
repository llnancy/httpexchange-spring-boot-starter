package io.github.llnancy.webclient.core;

import io.github.llnancy.webclient.util.WebClientUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;

/**
 * webclient factory bean
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
public class WebClientFactoryBean<T> implements FactoryBean<T>, EnvironmentAware {

    private final Class<T> webClientInterface;

    private Environment environment;

    public WebClientFactoryBean(Class<T> webClientInterface) {
        this.webClientInterface = webClientInterface;
    }

    @Override
    public T getObject() throws Exception {
        return createHttpServiceProxyFactory().createClient(webClientInterface);
    }

    private HttpServiceProxyFactory createHttpServiceProxyFactory() {
        return HttpServiceProxyFactory.builder(createWebClientAdapter()).build();
    }

    private WebClientAdapter createWebClientAdapter() {
        return WebClientAdapter.forClient(createWebClient());
    }

    private WebClient createWebClient() {
        io.github.llnancy.webclient.core.WebClient webClient =
                AnnotatedElementUtils.findMergedAnnotation(webClientInterface, io.github.llnancy.webclient.core.WebClient.class);
        String baseUrl = WebClientUtils.convertBaseUrl(Objects.requireNonNull(webClient).baseUrl(), environment);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, ClientResponse::createException)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return this.webClientInterface;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
