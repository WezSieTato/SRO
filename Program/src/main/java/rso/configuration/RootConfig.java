package rso.configuration;

/**
 * Created by Jaroslaw on 2015-04-28.
 */
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import rso.middleware.JPAConfig;

@Configuration
@ComponentScan(
        basePackages = {"rso"}
)
@PropertySource(value = {"classpath:application.properties"})
@Import({ JPAConfig.class})
public class RootConfig {
}