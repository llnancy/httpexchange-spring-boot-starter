package io.github.llnancy.httpexchange.core;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aot.AotDetector;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * http exchange client scanner
 *
 * @author llnancy admin@lilu.org.cn
 * @see HttpExchangeClientFactoryBean
 * @since JDK17 2023/6/29
 */
@Slf4j
public class ClassPathHttpExchangeClientScanner extends ClassPathBeanDefinitionScanner {

    private final ClassLoader classLoader;

    @SuppressWarnings("rawtypes")
    private Class<? extends HttpExchangeClientFactoryBean> httpExchangeClientFactoryBeanClass = HttpExchangeClientFactoryBean.class;

    /**
     * set default scope
     */
    @Setter
    private String defaultScope;

    /**
     * set class of {@link HttpExchangeClientFactoryBean}
     *
     * @param httpExchangeClientFactoryBeanClass class of {@link HttpExchangeClientFactoryBean}
     */
    @SuppressWarnings("rawtypes")
    public void setHttpExchangeClientFactoryBeanClass(Class<? extends HttpExchangeClientFactoryBean> httpExchangeClientFactoryBeanClass) {
        this.httpExchangeClientFactoryBeanClass = httpExchangeClientFactoryBeanClass == null ? HttpExchangeClientFactoryBean.class : httpExchangeClientFactoryBeanClass;
    }

    /**
     * constructor
     *
     * @param registry    {@link BeanDefinitionRegistry}
     * @param classLoader {@link ClassLoader}
     */
    public ClassPathHttpExchangeClientScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry);
        this.classLoader = classLoader;
        setIncludeAnnotationConfig(!AotDetector.useGeneratedArtifacts());
    }

    /**
     * register @HttpExchangeClient filter
     */
    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(HttpExchangeClient.class);
        this.addIncludeFilter(annotationTypeFilter);
    }

    @Override
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn("No HttpExchangeClient was found in '" + Arrays.toString(basePackages)
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
        AbstractBeanDefinition definition;
        BeanDefinitionRegistry registry = getRegistry();
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (AbstractBeanDefinition) holder.getBeanDefinition();
            boolean scopedProxy = false;
            if (ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())) {
                definition = (AbstractBeanDefinition) Optional
                        .ofNullable(((RootBeanDefinition) definition).getDecoratedDefinition())
                        .map(BeanDefinitionHolder::getBeanDefinition).orElseThrow(() -> new IllegalStateException(
                                "The target bean definition of scoped proxy bean not found. Root bean definition[" + holder + "]"));
                scopedProxy = true;
            }
            String beanClassName = definition.getBeanClassName();
            if (log.isDebugEnabled()) {
                log.debug("Creating ExchangeClientBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' Interface");
            }
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            Assert.notNull(beanClassName, "beanClassName must not be null.");
            try {
                Class<?> clazz = ClassUtils.forName(beanClassName, classLoader);
                definition.getPropertyValues().add("httpExchangeClientInterface", clazz);
                // Attribute for MockitoPostProcessor
                definition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, clazz);
            } catch (ClassNotFoundException ignore) {
                // ignore
            }
            definition.setBeanClass(this.httpExchangeClientFactoryBeanClass);

            if (scopedProxy) {
                continue;
            }
            if (ConfigurableBeanFactory.SCOPE_SINGLETON.equals(definition.getScope()) && StringUtils.hasText(defaultScope)) {
                definition.setScope(defaultScope);
            }
            if (!definition.isSingleton()) {
                Assert.notNull(registry, "registry must not be null.");
                BeanDefinitionHolder proxyHolder = ScopedProxyUtils.createScopedProxy(holder, registry, true);
                String beanName = proxyHolder.getBeanName();
                if (registry.containsBeanDefinition(beanName)) {
                    registry.removeBeanDefinition(beanName);
                }
                registry.registerBeanDefinition(beanName, proxyHolder.getBeanDefinition());
            }
        }
    }
}
