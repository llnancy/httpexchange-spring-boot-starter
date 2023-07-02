package io.github.llnancy.httpexchange.config;

import io.github.llnancy.httpexchange.core.AutoConfiguredExchangeClientScannerRegistrar;
import io.github.llnancy.httpexchange.core.ExchangeClientFactoryBean;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

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

    // @Bean
    // static ExchangeClientBeanFactoryInitializationAotProcessor exchangeClientBeanFactoryInitializationAotProcessor(GenericApplicationContext applicationContext) {
    //     return new ExchangeClientBeanFactoryInitializationAotProcessor(applicationContext);
    // }
}

class ExchangeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		if (!ClassUtils.isPresent("io.github.llnancy.httpexchange", classLoader)) {
			return;
		}
		hints.reflection().registerType(TypeReference.of(ExchangeClientFactoryBean.class),
				hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
						MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.DECLARED_FIELDS));
	}

}
