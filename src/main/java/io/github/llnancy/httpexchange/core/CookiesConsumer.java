package io.github.llnancy.httpexchange.core;

import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

/**
 * {@link WebClient.Builder#defaultCookies(Consumer)} holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/7/12
 */
public interface CookiesConsumer {

    /**
     * hold a consumer of cookies map
     *
     * @return {@link Consumer<MultiValueMap<String, String>>}
     */
    Consumer<MultiValueMap<String, String>> consume();
}
