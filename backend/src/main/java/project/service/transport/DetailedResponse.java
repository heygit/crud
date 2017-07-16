package project.service.transport;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailedResponse<T> {

    private T pojo;
    private MultivaluedMap<String, String> headers;
    private Map<String, String> cookies;

    public void setPojo(T object) {
        this.pojo = object;
    }

    public T getPojo() {
        return this.pojo;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(List<? extends Cookie> cookies) {
        if (cookies == null) {
            return;
        }
        if (this.cookies == null) {
            this.cookies = new HashMap<>();
        }
        for (Cookie cookie: cookies) {
            this.cookies.put(cookie.getName(), cookie.getValue());
        }
    }
}