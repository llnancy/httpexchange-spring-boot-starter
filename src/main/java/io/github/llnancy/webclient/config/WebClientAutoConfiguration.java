package io.github.llnancy.webclient.config;

import io.github.llnancy.webclient.core.AutoConfiguredWebClientScannerRegistrar;
import io.github.llnancy.webclient.core.WebClientFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * webclient auto configuration
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@AutoConfiguration
public class WebClientAutoConfiguration {

    @Configuration
    @Import({AutoConfiguredWebClientScannerRegistrar.class})
    @ConditionalOnMissingBean(WebClientFactoryBean.class)
    public static class RetrofitScannerRegistrarNotFoundConfiguration {
    }
}
