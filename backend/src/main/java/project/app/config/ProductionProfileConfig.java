package project.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.dao.impl.CustomRepositoryImpl;

@Configuration
@Profile("production")
@EntityScan("project")
@EnableJpaRepositories(value = "project", repositoryBaseClass = CustomRepositoryImpl.class)
@ComponentScan({"project.dao.impl", "project.service.impl"})
public class ProductionProfileConfig {

}
