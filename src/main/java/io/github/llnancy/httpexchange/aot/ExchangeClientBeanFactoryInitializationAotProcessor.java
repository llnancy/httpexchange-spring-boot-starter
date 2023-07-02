// package io.github.llnancy.httpexchange.aot;
//
// import io.github.llnancy.httpexchange.core.ExchangeClientFactoryBean;
// import org.springframework.aot.generate.GenerationContext;
// import org.springframework.aot.hint.BindingReflectionHintsRegistrar;
// import org.springframework.beans.factory.BeanFactoryUtils;
// import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
// import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
// import org.springframework.beans.factory.aot.BeanFactoryInitializationCode;
// import org.springframework.beans.factory.aot.BeanRegistrationExcludeFilter;
// import org.springframework.beans.factory.config.BeanDefinition;
// import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
// import org.springframework.beans.factory.support.RegisteredBean;
// import org.springframework.context.support.GenericApplicationContext;
//
// import java.util.Map;
// import java.util.stream.Collectors;
//
// /**
//  * A {@link BeanFactoryInitializationAotProcessor} that creates an
//  * {@link BeanFactoryInitializationAotContribution} that registers bean definitions and
//  * proxy and reflection hints for http exchange client beans.
//  *
//  * @author llnancy admin@lilu.org.cn
//  * @since JDK17 2023/7/2
//  */
// public class ExchangeClientBeanFactoryInitializationAotProcessor implements BeanRegistrationExcludeFilter, BeanFactoryInitializationAotProcessor {
//
//     private final GenericApplicationContext context;
//
//     private final Map<String, BeanDefinition> exchangeClientBeanDefinitions;
//
//     private final BindingReflectionHintsRegistrar bindingRegistrar = new BindingReflectionHintsRegistrar();
//
//     public ExchangeClientBeanFactoryInitializationAotProcessor(GenericApplicationContext context) {
//         this.context = context;
//         this.exchangeClientBeanDefinitions = getExchangeClientBeanDefinitions();
//     }
//
//     private Map<String, BeanDefinition> getExchangeClientBeanDefinitions() {
//         return context.getBeansOfType(ExchangeClientFactoryBean.class)
//                 .keySet()
//                 .stream()
//                 .map(beanName -> {
//                     String originalBeanName = BeanFactoryUtils.transformedBeanName(beanName);
//                     return Map.entry(originalBeanName, context.getBeanDefinition(originalBeanName));
//                 })
//                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//     }
//
//     @Override
//     @SuppressWarnings("NullableProblems")
//     public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
//         ConfigurableListableBeanFactory contextBeanFactory = context.getBeanFactory();
//         if (exchangeClientBeanDefinitions.isEmpty() || !beanFactory.equals(contextBeanFactory)) {
//             return null;
//         }
//         return new AotContribution(exchangeClientBeanDefinitions);
//     }
//
//     @Override
//     public boolean isExcludedFromAotProcessing(RegisteredBean registeredBean) {
//         return registeredBean.getBeanClass().equals(ExchangeClientFactoryBean.class)
//                 || exchangeClientBeanDefinitions.containsKey(registeredBean.getBeanClass().getName());
//     }
//
//     final class AotContribution implements BeanFactoryInitializationAotContribution {
//
//         private final Map<String, BeanDefinition> exchangeClientBeanDefinitions;
//
//         public AotContribution(Map<String, BeanDefinition> exchangeClientBeanDefinitions) {
//             this.exchangeClientBeanDefinitions = exchangeClientBeanDefinitions;
//         }
//
//         @Override
//         public void applyTo(GenerationContext generationContext, BeanFactoryInitializationCode beanFactoryInitializationCode) {
//
//         }
//     }
// }
