package io.github.llnancy.httpexchange.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * exchange client scanner
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@Slf4j
public class ClassPathExchangeClientScanner extends ClassPathBeanDefinitionScanner {

    private final ClassLoader classLoader;

    public ClassPathExchangeClientScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry);
        this.classLoader = classLoader;
    }

    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(ExchangeClient.class);
        this.addIncludeFilter(annotationTypeFilter);
    }

    @Override
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn("No ExchangeClient was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata().isInterface()) {
            try {
                Class<?> target = ClassUtils.forName(
                        beanDefinition.getMetadata().getClassName(),
                        classLoader
                );
                return !target.isAnnotation();
            } catch (Exception ex) {
                log.error("load class exception:", ex);
            }
        }
        return false;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            if (log.isDebugEnabled()) {
                log.debug("Creating ExchangeClientBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' Interface");
            }
            definition.getConstructorArgumentValues()
                    .addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
            // beanClass 全部设置为 ExchangeClientFactoryBean
            definition.setBeanClass(ExchangeClientFactoryBean.class);
        }
    }
}
