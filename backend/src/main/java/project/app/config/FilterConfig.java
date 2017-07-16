package project.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.filter.SessionIdHandlerLog4j;

@Configuration
public class FilterConfig {

    private static final String ALL_URLS_PATTERN = "/*";

    private static final String SESSION_ID_HANDLER_LOG4J_FILTER_NAME = "SessionIdHandlerLog4j";

    @Bean
    public FilterRegistrationBean registerSessionIdHandlerLog4j() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SessionIdHandlerLog4j());
        registration.addUrlPatterns(ALL_URLS_PATTERN);
        registration.setName(SESSION_ID_HANDLER_LOG4J_FILTER_NAME);
        registration.setOrder(1);
        return registration;
    }

}
