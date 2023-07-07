# httpexchange-spring-boot-starter

[![Build](https://img.shields.io/github/actions/workflow/status/llnancy/httpexchange-spring-boot-starter/build.yml?branch=master)](https://github.com/llnancy/httpexchange-spring-boot-starter/actions)
[![JDK Version](https://img.shields.io/badge/JDK-17-0e83c)](https://www.oracle.com/java/technologies/javase/17all-relnotes.html)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.llnancy/httpexchange-spring-boot-starter)](https://search.maven.org/artifact/io.github.llnancy/httpexchange-spring-boot-starter)
[![License: Apache-2.0](https://img.shields.io/github/license/llnancy/httpexchange-spring-boot-starter?color=FF5531)](https://opensource.org/license/apache-2-0/)

像 `@FeignClient` 和 `@RetrofitClient` 那样使用 `Spring Boot 3.x` 中的 `HTTP Interface`。

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>io.github.llnancy</groupId>
    <artifactId>httpexchange-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 定义 HTTP 接口

**接口必须使用 `@io.github.llnancy.httpexchange.core.ExchangeClient` 注解标记**！`HTTP` 相关注解可参考官方文档：[Spring 官方文档](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface-method-parameters)。

```java
@ExchangeClient(baseUrl = "${test.baseUrl}")
public interface HttpApi {

    @PostExchange("/post")
    Mono<Map<String, Object>> post(@RequestBody Map<String, Object> params);
}
```

### 注入使用

**将接口注入到其它 `Service` 中即可使用！**

```java
@Service
public class TestService {

    @Autowired
    private HttpApi httpApi;

    public void test() {
       // 使用 httpApi 发起 HTTP 请求
    }
}
```

## Q & A

### 报错 Caused by: org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer : 262144

```text
Caused by: org.springframework.core.io.buffer.DataBufferLimitException: Exceeded limit on max bytes to buffer : 262144
	at org.springframework.core.io.buffer.LimitedDataBufferList.raiseLimitException(LimitedDataBufferList.java:99)
```

具体参考 [https://www.baeldung.com/spring-webflux-databufferlimitexception](https://www.baeldung.com/spring-webflux-databufferlimitexception)

本框架支持的方式如下：

1. 自定义一个 `io.github.llnancy.httpexchange.core.ClientCodecConfigurerConsumer`

    ```java
        @Component
        class MyClientCodecConfigurerConsumer implements ClientCodecConfigurerConsumer {
    
            @Override
            public Consumer<ClientCodecConfigurer> consumer() {
                return configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(500 * 1024);
            }
        }
    ```

2. 在 `@ExchangeClient` 注解中指定 `codecConfigurerConsumer`

    ```java
    @ExchangeClient(baseUrl = "https://lilu.org.cn", codecConfigurerConsumer = MyClientCodecConfigurerConsumer.class)
    public interface MyExchangeClient {
    }
    ```

### 支持编译为 `native` 镜像吗？

支持。

## License

[Apache License 2.0](https://github.com/llnancy/httpexchange-spring-boot-starter/blob/master/LICENSE)

Copyright (c) 2023-present llnancy
