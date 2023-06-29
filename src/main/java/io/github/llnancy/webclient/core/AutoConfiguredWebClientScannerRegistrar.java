package io.github.llnancy.webclient.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * auto configure webclient scanner
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@Slf4j
public class AutoConfiguredWebClientScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware {

    private ClassLoader classLoader;

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            log.warn("Could not determine auto-configuration package, automatic webclient scanning disabled.");
            return;
        }
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        // Scan the @WebClient annotated interface under the specified path and register it to the
        // BeanDefinitionRegistry
        ClassPathWebClientScanner scanner = new ClassPathWebClientScanner(registry, classLoader);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        String[] packageArr = packages.toArray(new String[0]);
        log.info("Scan the @WebClient annotated interface using the auto-configuration package. packages={}", Arrays.toString(packageArr));
        scanner.registerFilters();
        // Scan and register to BeanDefinition
        scanner.doScan(packageArr);
    }
}
