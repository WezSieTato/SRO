package rso.program.configuration;

/**
 * Created by Jaroslaw on 2015-04-28.
 */
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import rso.core.configuration.JPAConfig;
import rso.middleware.configuration.MiddlewareConfig;
import rso.server.configuration.ServerConfig;

@Configuration
@ComponentScan(
        basePackages = {"rso"}
)
@PropertySource(value = {"file:application.properties"})
@Import({ ServerConfig.class, MiddlewareConfig.class, JPAConfig.class})
public class RootConfig {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
                return new PropertySourcesPlaceholderConfigurer();
        }
}