package io.github.llnancy.exchangeclient.config;

import io.github.llnancy.exchangeclient.core.AutoConfiguredExchangeClientScannerRegistrar;
import io.github.llnancy.exchangeclient.core.ExchangeClientFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * exchange client auto configuration
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@AutoConfiguration
public class ExchangeClientAutoConfiguration {

    @Configuration
    @Import({AutoConfiguredExchangeClientScannerRegistrar.class})
    @ConditionalOnMissingBean(ExchangeClientFactoryBean.class)
    public static class ExchangeScannerRegistrarNotFoundConfiguration {
    }
}
