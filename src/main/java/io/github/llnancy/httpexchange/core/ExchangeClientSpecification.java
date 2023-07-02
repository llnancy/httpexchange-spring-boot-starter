package io.github.llnancy.httpexchange.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * exchange client Specification
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK8 2023/7/2
 */
@Setter
@ToString
@EqualsAndHashCode
public class ExchangeClientSpecification implements NamedContextFactory.Specification {

    private String name;

    @Getter
    private String className;

    private Class<?>[] configuration;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return this.configuration;
    }
}
