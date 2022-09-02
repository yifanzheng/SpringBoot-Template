package top.yifan.config.apidoc;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerAutoConfiguration {
    
    @Value("${swagger.scanPackage: top.yifancommon.web.rest}")
    private String scanPackage;
    
    @Autowired
    private SwaggerProperties swaggerProperties;
    
    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> apis = null;
        if (swaggerProperties.isActuatorEnable()) {
            apis = Predicates.or(RequestHandlerSelectors.basePackage(scanPackage),
                    RequestHandlerSelectors.basePackage("org.springframework.boot.actuate.endpoint.web.servlet"));
        } else {
            RequestHandlerSelectors.basePackage(scanPackage);
        }
        
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(apis)
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContext())
                .tags(
                    new Tag("AMS", "AMS 接口"),
                    getTags()
                );
    }

    private Tag[] getTags() {
        Map<String, String> tagsMap = swaggerProperties.getTags();
        if (CollectionUtils.isEmpty(tagsMap)) {
            return new Tag[0];
        }
        List<Tag> tags = new ArrayList<>();
        // 默认的Tags
        tags.add(new Tag("z-others", ""));
        tags.add(new Tag("z-mock", ""));
        tags.add(new Tag("web-mvc-endpoint-handler-mapping", "actuator"));
        tags.add(new Tag("operation-handler", "actuator"));
        // 来自于配置的Tags
        for (Entry<String, String> e : tagsMap.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            tags.add(new Tag(k, v));
        }
        return tags.toArray(new Tag[0]);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful APIs")
                .description("")
                .termsOfServiceUrl("")
                .contact(new Contact("DAE Spring-boot2.0", null, null))
                .version("1.0")
                .build();
    }
    
    private List<ApiKey> securitySchemes() {
        // 参数：显示名称，key名称，type
        return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
    }
    
    private List<SecurityContext> securityContext() {
        // 配置需要添加认证的路径
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex(".*")) // ^(?api).*$ ^(api).
                        .build()
        );
    }
    
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "AccessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("Authorization", authorizationScopes));
    }
    
    @Bean
    public AlternateTypeRuleConvention pageableConvention(
            final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                        new AlternateTypeRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
                );
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class, "page"),
                        property(Integer.class, "size"),
                        property(String.class, "sort")
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }
    
}