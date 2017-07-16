package project.service.transport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RESTResources {

    @Value("${transport.tbapi.url}")
    private String BUSINESS_API_URL;

    @PostConstruct
    public void initValues() {
        BUSINESS_API = new RESTResource(BUSINESS_API_URL);
    }

    public void setTBAPI(String url) {
        this.BUSINESS_API.update(url);
    }

    public RESTResource BUSINESS_API;

    public class RESTResource {

        private String url;

        public RESTResource(String url) {
            this.url = url;
        }

        public void update(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return this.url;
        }
    }
}