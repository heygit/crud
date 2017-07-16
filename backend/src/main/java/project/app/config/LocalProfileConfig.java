package project.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@ComponentScan({"project.dao.stub", "project.service.stub"})
public class LocalProfileConfig {

}
