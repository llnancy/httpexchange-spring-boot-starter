package io.github.llnancy.httpexchange.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * utils
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/30
 */
@UtilityClass
@Slf4j
public final class ApplicationContextUtils {

    /**
     * 优先从 Spring 容器获取实例，如果获取失败，调用无参方法创建，如果再失败，尝试调用无参 create 静态方法创建
     *
     * @param context spring context
     * @param clazz   对象类型
     * @param <T>     泛型参数
     * @return spring bean，或者反射创建的实例。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanOrReflect(ApplicationContext context, Class<T> clazz) {
        try {
            return context.getBean(clazz);
        } catch (Exception e1) {
            try {
                log.warn("Failed to get bean from applicationContext！", e1);
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e2) {
                log.warn("Failed to create instance by reflect.", e2);
                try {
                    return (T) clazz.getMethod("create").invoke(null);
                } catch (Exception e3) {
                    throw new RuntimeException("Failed to create instance through create static method.", e3);
                }
            }
        }
    }
}
