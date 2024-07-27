package io.github.llnancy.httpexchange.core;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * {@link WebClient.Builder#defaultUriVariables(Map)} supplier
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/7/12
 */
public interface DefaultUriVariablesSupplier {

    /**
     * supply defaultUriVariables Map
     *
     * @return Map
     */
    Map<String, ?> supply();
}
