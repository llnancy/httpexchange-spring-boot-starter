package io.github.llnancy.httpexchange.core;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@link WebClient.Builder#defaultStatusHandler(Predicate, Function)} holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/7/12
 */
public interface DefaultStatusHandlerHolder {

    /**
     * to match responses with
     *
     * @return {@link Predicate<HttpStatusCode>}
     */
    Predicate<HttpStatusCode> statusPredicate();

    /**
     * to map the response to an error signal
     *
     * @return {@link Function<ClientResponse, Mono<? extends Throwable>>}
     */
    Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction();
}
