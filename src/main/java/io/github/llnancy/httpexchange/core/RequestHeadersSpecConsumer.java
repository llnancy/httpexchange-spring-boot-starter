package io.github.llnancy.httpexchange.core;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

/**
 * {@link WebClient.Builder#defaultRequest(Consumer)} holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/7/12
 */
public interface RequestHeadersSpecConsumer {

    /**
     * hold a consumer of {@link WebClient.RequestHeadersSpec<?>}
     *
     * @return {@link Consumer<WebClient.RequestHeadersSpec<?>>}
     */
    Consumer<WebClient.RequestHeadersSpec<?>> consume();
}
