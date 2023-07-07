package io.github.llnancy.httpexchange.core;

import org.springframework.http.codec.ClientCodecConfigurer;

import java.util.function.Consumer;

/**
 * {@link ClientCodecConfigurer} consumer holder
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/30
 */
public interface ClientCodecConfigurerConsumer {

    /**
     * hold a consumer of {@link ClientCodecConfigurer}
     *
     * @return {@link Consumer<ClientCodecConfigurer>}
     */
    Consumer<ClientCodecConfigurer> consumer();
}
