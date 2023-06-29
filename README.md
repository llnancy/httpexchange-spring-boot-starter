# webclient-spring-boot-starter

像 `@FeignClient` 和 `@RetrofitClient` 那样使用 `Spring Boot 3.x` 中的 `HTTP Interface`。

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>io.github.llnancy</groupId>
    <artifactId>webclient-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 定义 HTTP 接口

**接口必须使用 `@io.github.llnancy.webclient.core.WebClient` 注解标记**！`HTTP` 相关注解可参考官方文档：[Spring 官方文档](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface-method-parameters)。

```java
@WebClient(baseUrl = "${test.baseUrl}")
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
