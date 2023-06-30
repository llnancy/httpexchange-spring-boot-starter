package io.github.llnancy.exchangeclient.util;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * utils
 *
 * @author llnancy admin@lilu.org.cn
 * @since JDK17 2023/6/29
 */
public class ExchangeClientUtils {

    private static final String SUFFIX = "/";

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
