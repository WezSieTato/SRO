package rso.program.configuration;

/**
 * Created by Jaroslaw on 2015-04-28.
 */
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import rso.core.configuration.JPAConfig;

@Configuration
@ComponentScan(
        basePackages = {"rso"}
)
@PropertySource(value = {"file:application.properties"})
@Import({ JPAConfig.class})
public class RootConfig {
}