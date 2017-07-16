package project.service.transport;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public class RESTRequestParameters {

    public static final String RANGE_REQUEST_HEADER = "Range";
    public static final String RANGE_RESPONSE_HEADER = "Content-Range";
    public static final String RESULT_COUNT_HEADER_NAME ="ResultCount";
    public static final String SORT_QUERY_PARAMETER = "sort";
    public static final String FIELDS_QUERY_PARAMETER = "fields";
    private final static RESTRequestParameters EMPTY_PARAMS = new RESTRequestParameters();
    private Map<String, String> pathParams = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> cookieParams = new HashMap<>();
    private MultivaluedMap<String, String> headers = new MultivaluedMapImpl();

    /**
     * Return empty request params
     * Do not modify this object!
     * @return
     */
    public static RESTRequestParameters emptyParams() {
        return EMPTY_PARAMS;
    }

    /**
     * Adds query parameter
     * protocol://domain/path?query
     * @param key
     * @param value
     */
    public void addQueryParameter(String key, String value) {
        this.queryParams.put(key, value);
    }

    /**
     * Adds query parameter if not exist
     * protocol://domain/path?query
     * @param key
     * @param value
     */
    public void addQueryParameterIfNotExist(String key, String value) {
        if (!queryParams.containsKey(key)) {
            queryParams.put(key, value);
        }
    }

    /**
     * Fills path parameter
     * protocol://domain/path?query
     * @param key
     * @param value
     */
    public void addPathParameter(String key, String value) {
        this.pathParams.put(key, value);
    }

    /**
     * Fills path cookies
     * @param key
     * @param value
     */
    public void addCookieParameter(String key, String value) {
        this.cookieParams.put(key, value);
    }

    public void addCookieParameters(Map<String, String> cookies) {
        this.cookieParams.putAll(cookies);
    }

    /**
     * Adds header
     * @param name
     * @param value
     */
    public void addHeader(String name, String value)
    {
        this.headers.add(name, value);
    }

    public Map<String, String> getQueryParams() {
        return this.queryParams;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public Map<String, String> getCookieParams() {
        return cookieParams;
    }


    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "RESTRequestParameters{" +
                "pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                ", cookieParams=" + cookieParams +
                ", headers=" + headers +
                '}';
    }
}