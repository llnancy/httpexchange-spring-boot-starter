package io.github.llnancy.httpexchange.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * utils
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
@UtilityClass
public class HttpExchangeClientUtils {

    private static final String SUFFIX = "/";

    /**
     * convert base url
     *
     * @param baseUrl     source baseUrl
     * @param environment {@link Environment}
     * @return target baseUrl
     */
    public static String convertBaseUrl(String baseUrl, Environment environment) {
        if (StringUtils.hasText(baseUrl)) {
            baseUrl = environment.resolveRequiredPlaceholders(baseUrl);
            // 解析 baseUrl 占位符
            if (!baseUrl.endsWith(SUFFIX)) {
                baseUrl += SUFFIX;
            }
        }
        return baseUrl;
    }
}
