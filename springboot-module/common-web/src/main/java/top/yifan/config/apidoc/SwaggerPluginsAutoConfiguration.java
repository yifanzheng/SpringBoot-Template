package top.yifan.config.apidoc;


import com.fasterxml.classmate.TypeResolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Register Springfox plugins.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(Docket.class)
@AutoConfigureAfter(SwaggerAutoConfiguration.class)
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class SwaggerPluginsAutoConfiguration {

    @Configuration
    @ConditionalOnClass(Pageable.class)
    @ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
    public static class SpringPagePluginConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor typeNameExtractor,
                                                                             TypeResolver typeResolver) {
            return new PageableParameterBuilderPlugin(typeNameExtractor, typeResolver);
        }
    }
}