package io.github.llnancy.httpexchange.core;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

/**
 * {@link WebClient.Builder#defaultHeaders(Consumer)} holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK8 2023/7/1
 */
public interface HttpHeadersConsumer {

    Consumer<HttpHeaders> consumer();
}
