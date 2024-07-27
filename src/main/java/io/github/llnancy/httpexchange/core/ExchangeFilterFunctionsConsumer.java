package io.github.llnancy.httpexchange.core;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.function.Consumer;

/**
 * {@link WebClient.Builder#filters(Consumer)} holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2024/7/12
 */
public interface ExchangeFilterFunctionsConsumer {

    /**
     * hold a consumer of {@link ExchangeFilterFunction} list
     *
     * @return {@link Consumer<List<ExchangeFilterFunction>>}
     */
    Consumer<List<ExchangeFilterFunction>> consume();
}
