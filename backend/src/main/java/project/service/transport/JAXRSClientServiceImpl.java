package project.service.transport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.service.async.AsynchronousExecutionService;
import project.service.transport.exception.*;
import project.service.transport.exception.model.TransportErrorInfo;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class JAXRSClientServiceImpl implements JAXRSClientService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(JAXRSClientServiceImpl.class);
    private static final String ENCODING = "UTF-8";
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_DEFAULT_USERNAME = "anonymous";
    private static final String AUTH_HEADER_USERNAME_PLACE = "{username}";
    private static final String AUTH_HEADER_VALUE = "Trusted application=\"SSP\", username=\"" +
            AUTH_HEADER_USERNAME_PLACE + "\"";

    private Client client;
    private ObjectMapper jsonSerializer;

    private final ServiceResponseAnalyzer responseAnalyzer;

    private final AsynchronousExecutionService asynchronousExecutionService;

    @Autowired
    public JAXRSClientServiceImpl(ServiceResponseAnalyzer responseAnalyzer,
                                  AsynchronousExecutionService asynchronousExecutionService) {
        client = new Client();
        jsonSerializer = new ObjectMapper();
        this.responseAnalyzer = responseAnalyzer;
        this.asynchronousExecutionService = asynchronousExecutionService;
    }

    public RequestBuilder resource(RESTResources.RESTResource resource) {
        return new RequestBuilder(resource.toString());
    }

    /**
     * Debug purpose only
     * TODO Remove
     */
    public RequestBuilder resource(String resource) {
        return new RequestBuilder(resource);
    }

    private MultivaluedMap<String, String> toMultivaluedMap(final Map<String, String> map) {
        MultivaluedMap<String, String> multivaluedMap = new MultivaluedMapImpl();
        if (map != null) {
            for (Map.Entry<String, String> mapParam : map.entrySet()) {
                //log.debug("queryParam: {}->{}", mapParam.getKey(), mapParam.getValue());
                multivaluedMap.putSingle(mapParam.getKey(), mapParam.getValue());
            }
        }

        return multivaluedMap;
    }

    private <T> T getValue(ClientResponse clientResponse, TypeReference<T> type) {
        checkResponseStatus(clientResponse);

        return responseAnalyzer.parseResponse(clientResponse.getEntityInputStream(), clientResponse.getStatus(),
                type, ENCODING);
    }

    private void checkResponseStatus(final ClientResponse clientResponse) {
        LOGGER.debug("{}", clientResponse.toString());
        final int responseStatus = clientResponse.getStatus();
        if (responseStatus != HttpStatus.OK.value() &&
                responseStatus != HttpStatus.CREATED.value() &&
                responseStatus != NO_CONTENT) {
            if (HttpStatus.INTERNAL_SERVER_ERROR.value() == responseStatus) {
                try {
                    String response = IOUtils.toString(clientResponse.getEntityInputStream());
                    LOGGER.error("response = {}", response);
                    throw new InternalServerErrorException(responseAnalyzer.parseErrorResponse(response, TransportErrorInfo.class));
                } catch (IOException ex) {
                    LOGGER.error("Could not read response stream", ex);
                }
                throw new ServiceUnavailableException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
            if (HttpStatus.SERVICE_UNAVAILABLE.value() == responseStatus) {
                throw new ServiceUnavailableException("Service unavailable.");
            }
            if (HttpStatus.UNAUTHORIZED.value() == responseStatus) {
                throw new UnauthorizedException("Unauthorized Exception.");
            }
            if (HttpStatus.NOT_FOUND.value() == responseStatus) {
                try {
                    String response = IOUtils.toString(clientResponse.getEntityInputStream());
                    LOGGER.error("response = {}", response);
                } catch (IOException ex) {
                    LOGGER.error("Could not read response stream", ex);
                }
                throw new NotFoundException("Not found");
            }
            if (HttpStatus.UNPROCESSABLE_ENTITY.value() == responseStatus) {
                try {
                    String response = IOUtils.toString(clientResponse.getEntityInputStream());
                    LOGGER.error("response = {}", response);
                    throw new UnprocessableException(responseAnalyzer.parseErrorResponse(response, TransportErrorInfo.class));
                } catch (IOException ex) {
                    LOGGER.error("Could not read response stream", ex);
                }
                throw new ServiceUnavailableException(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
            }
            throw new RestServiceException("Unexpected error occurred during REST service call. "
                    + responseStatus + " " + clientResponse.toString(), null);
        }
    }

    @Override
    public <T> T get(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
                     TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                headers(parameters.getHeaders()).
                get(type);
    }

    @Override
    public byte[] getFile(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters) {
        ClientResponse clientResponse = this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                headers(parameters.getHeaders()).
                get();
        InputStream is = clientResponse.getEntityInputStream();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <T> DetailedResponse<T> getDetailed(RESTResources.RESTResource resource, RESTMethods method,
                                               RESTRequestParameters parameters, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                headers(parameters.getHeaders()).
                getDetailed(type);
    }

    @Override
    public <T> Future<T> asyncGet(RESTResources.RESTResource resource, RESTMethods method,
                                  RESTRequestParameters parameters, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                headers(parameters.getHeaders()).
                asyncGet(type);
    }

    @Override
    public <T> T put(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
                     Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                put(type);
    }

    @Override
    public <T> Future<T> asyncPut(RESTResources.RESTResource resource, RESTMethods method,
                                  RESTRequestParameters parameters, Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                asyncPut(type);
    }

    @Override
    public <T> T post(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
                      Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                post(type);
    }

    @Override
    public <T> DetailedResponse<T> postDetailed(RESTResources.RESTResource resource, RESTMethods method,
                                                RESTRequestParameters parameters, Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                postDetailed(type);
    }

    @Override
    public <T> Future<T> asyncPost(RESTResources.RESTResource resource, RESTMethods method,
                                   RESTRequestParameters parameters, Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                asyncPost(type);
    }

    @Override
    public <T> T delete(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
                        Object data, TypeReference<T> type) {
        return this.resource(resource).
                path(method.toString(), parameters.getPathParams()).
                queryParams(parameters.getQueryParams()).
                cookieParams(parameters.getCookieParams()).
                requestEntity(data).
                headers(parameters.getHeaders()).
                delete(type);
    }

    public class RequestBuilder {
        private static final String ACCEPT =
                "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,image/webp,*/*;q=0.8";

        private WebResource webResource;
        private AsyncWebResource asyncWebResource;
        private String resource;
        private String object;
        private String path;
        private Map<String, String> queryParams;
        private Map<String, String> cookieParams;
        private MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
        private String type = "application/json";
        //Integer value that specifies the read timeout interval in milliseconds.
        // If the property is 0 or not set, then the interval is set to infinity.
        private int readTimeout;
        //Integer value that specifies the connect timeout interval in milliseconds.
        // If the property is 0 or not set, then the interval is set to infinity.
        private int connectTimeout;

        public RequestBuilder(final String resource) {
            checkNotNull(resource, "null resource is passed");
            this.resource = resource;
            this.path = StringUtils.EMPTY;

        }

        public RequestBuilder headers(final MultivaluedMap<String, String> headers) {
            this.headers.putAll(headers);

            return this;
        }

        public RequestBuilder path(final String path, final Map<String, String> pathParams) {
            this.path = path;

            if (path == null) {
                //LOGGER.debug("null path is passed to 'path' method");
            } else {
                for (Map.Entry<String, String> param : pathParams.entrySet()) {
                    this.path = this.path.replaceAll("\\{" + param.getKey() + "\\}", param.getValue());
                }
            }

            return this;
        }

        public RequestBuilder queryParams(final Map<String, String> queryParams) {
            if (queryParams == null || queryParams.isEmpty()) {
                //LOGGER.debug("null or empty map is passed to 'queryParams' method");
                return this;
            }
            this.queryParams = queryParams;

            return this;
        }

        public RequestBuilder queryParam(final String key, final String value) {
            if (this.queryParams == null) {
                this.queryParams = new LinkedHashMap<>();
            }
            queryParams.put(key, value);

            return this;
        }

        public RequestBuilder cookieParams(final Map<String, String> cookieParams) {
            if (cookieParams == null || cookieParams.isEmpty()) {
                //LOGGER.debug("null or empty map is passed to 'cookieParams' method");
                return this;
            }
            if (this.cookieParams == null) {
                this.cookieParams = new HashMap<>();
            }
            this.cookieParams.putAll(cookieParams);

            return this;
        }

        public RequestBuilder cookieParams(final String key, final String value) {
            if (this.cookieParams == null) {
                this.cookieParams = new LinkedHashMap<>();
            }
            cookieParams.put(key, value);

            return this;
        }

        public RequestBuilder readTimeout(final int readTimeout) {
            this.readTimeout = readTimeout;

            return this;
        }

        public RequestBuilder connectTimeout(final int connectTimeout) {
            this.connectTimeout = connectTimeout;

            return this;
        }

        public RequestBuilder requestEntity(final Object entity) {
            try {
                object = jsonSerializer.writeValueAsString(entity);
                return this;
            } catch (Exception e) {
                LOGGER.error("Error occured during serializing Object: {}.\n{}", entity, e);
                throw new SerializationException("Error occured during serializing Object: " + entity, e);
            }
        }

        public RequestBuilder type(final String type) {
            if (type == null) {
                LOGGER.debug("null path is passed to 'type' method");
            }
            this.type = type;

            return this;
        }

        private WebResource.Builder prepare(final String method) {
            webResource = client.resource(resource);
            LOGGER.debug("{} {}{}", method, webResource, path);
            webResource = webResource.path(path);
            webResource.setProperty(ClientConfig.PROPERTY_CONNECT_TIMEOUT, connectTimeout);
            webResource.setProperty(ClientConfig.PROPERTY_READ_TIMEOUT, readTimeout);

            if (queryParams != null && !queryParams.isEmpty()) {
                LOGGER.debug("Query Params: {}", queryParams);
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    if (StringUtils.isNotEmpty(entry.getValue())) {
                        webResource = webResource.queryParam(entry.getKey(), entry.getValue());
                    }
                }
            }

            WebResource.Builder resultBuilder = webResource.getRequestBuilder();

            if (headers != null && !headers.isEmpty()) {
                LOGGER.debug("Headers: {}", headers);
                for (Map.Entry<String, List<String>> entry : headers.entrySet())
                    for (String value : entry.getValue()) {
                        resultBuilder = resultBuilder.header(entry.getKey(), value);
                    }
            }

            if (cookieParams != null && !cookieParams.isEmpty()) {
                //TODO: Delete the logger after tests
                LOGGER.debug("Cookies: {}", cookieParams);
                for (Map.Entry<String, String> cookiesEntrySet : cookieParams.entrySet()) {
                    resultBuilder =
                            resultBuilder.cookie(new Cookie(cookiesEntrySet.getKey(), cookiesEntrySet.getValue()));
                }
            }

            if (object != null) {
                LOGGER.debug("JSON: {}", object);
            }

            return resultBuilder.accept(ACCEPT).type(type);
        }

        private AsyncWebResource.Builder prepareAsync(final String method) {
            asyncWebResource = client.asyncResource(resource);
            LOGGER.debug("{} {}{}", method, webResource, path);
            asyncWebResource = asyncWebResource.path(path);
            if (queryParams != null && !queryParams.isEmpty()) {
                LOGGER.debug("Query Params: {}", queryParams);
                asyncWebResource = asyncWebResource.queryParams(toMultivaluedMap(queryParams));
            }
            if (object != null) {
                LOGGER.debug("JSON: {}", object);
            }

            return asyncWebResource.accept(ACCEPT).type(type);
        }

        public <T> Future<T> asyncPut(final TypeReference<T> type) {
            return asynchronousExecutionService.execute(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    ClientResponse response = put();
                    return processResponse(response, HttpMethod.PUT, type);
                }
            });
        }

        public <T> Future<T> asyncPost(final TypeReference<T> type) {
            return asynchronousExecutionService.execute(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    ClientResponse response = post();
                    return processResponse(response, HttpMethod.POST, type);
                }
            });
        }

        public <T> Future<T> asyncGet(final TypeReference<T> type) {
            return asynchronousExecutionService.execute(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    ClientResponse response = get();
                    return processResponse(response, HttpMethod.GET, type);
                }
            });
        }

        public Future<ClientResponse> asyncPut() {
            return asynchronousExecutionService.execute(new Callable<ClientResponse>() {
                @Override
                public ClientResponse call() throws Exception {
                    ClientResponse response = prepare(HttpMethod.PUT).put(ClientResponse.class, object);
                    if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                        logError(HttpMethod.PUT, response);
                    } else {
                        LOGGER.debug("{}", response.toString());
                    }

                    return response;
                }
            });
        }

        public Future<ClientResponse> asyncPost() {
            return asynchronousExecutionService.execute(new Callable<ClientResponse>() {
                @Override
                public ClientResponse call() throws Exception {
                    ClientResponse response = prepare(HttpMethod.POST).post(ClientResponse.class, object);
                    if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                        logError(HttpMethod.POST, response);
                    } else {
                        LOGGER.debug("{}", response.toString());
                    }

                    return response;
                }
            });
        }

        public Future<ClientResponse> asyncGet() {
            return asynchronousExecutionService.execute(new Callable<ClientResponse>() {
                @Override
                public ClientResponse call() throws Exception {
                    ClientResponse response = prepare(HttpMethod.GET).get(ClientResponse.class);
                    if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                        logError(HttpMethod.GET, response);
                    } else {
                        LOGGER.debug("{}", response.toString());
                    }

                    return response;
                }
            });
        }

        public <T> T get(final TypeReference<T> type) {
            ClientResponse response = this.get();

            return processResponse(response, HttpMethod.GET, type);
        }

        public <T> DetailedResponse<T> getDetailed(final TypeReference<T> type) {
            ClientResponse response = this.get();

            DetailedResponse<T> result = new DetailedResponse<>();

            result.setPojo(processResponse(response, HttpMethod.GET, type));
            result.setHeaders(response.getHeaders());
            result.setCookies(response.getCookies());

            return result;
        }

        public ClientResponse get() {
            try {
                ClientResponse response = prepare(HttpMethod.GET).get(ClientResponse.class);
                if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                    logError(HttpMethod.GET, response);
                }
                return response;
            } catch (ClientHandlerException e) {
                throw new ServiceUnavailableException("Service unavailable", e);
            }
        }

        public <T> T put(final TypeReference<T> type) {
            ClientResponse response = this.put();

            return processResponse(response, HttpMethod.PUT, type);
        }

        public ClientResponse put() {
            try {
                ClientResponse response = prepare(HttpMethod.PUT).put(ClientResponse.class, this.object);
                if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                    logError(HttpMethod.PUT, response);
                }
                return response;
            } catch (ClientHandlerException e) {
                throw new ServiceUnavailableException("Service unavailable", e);
            }
        }

        public <T> T post(final TypeReference<T> type) {
            ClientResponse response = this.post();

            return processResponse(response, HttpMethod.POST, type);
        }

        public <T> DetailedResponse<T> postDetailed(final TypeReference<T> type) {
            ClientResponse response = this.post();

            DetailedResponse<T> result = new DetailedResponse<>();
            result.setPojo(processResponse(response, HttpMethod.POST, type));
            result.setHeaders(response.getHeaders());
            result.setCookies(response.getCookies());

            return result;
        }

        public ClientResponse post() {
            try {
                ClientResponse response = prepare(HttpMethod.POST).post(ClientResponse.class, this.object);
                if ((response.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                    logError(HttpMethod.POST, response);
                }
                return response;
            } catch (ClientHandlerException e) {
                throw new ServiceUnavailableException("Service unavailable", e);
            }
        }

        public <T> T delete(final TypeReference<T> type) {
            return getValue(this.delete(), type);
        }

        public ClientResponse delete() {
            try {
                final ClientResponse clientResponse = prepare(HttpMethod.DELETE).delete(ClientResponse.class);
                if ((clientResponse.getStatus() == ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                    logError(HttpMethod.DELETE, clientResponse);
                }
                LOGGER.debug("{}", clientResponse.toString());
                return clientResponse;
            } catch (ClientHandlerException e) {
                throw new ServiceUnavailableException("Service unavailable", e);
            }
        }

        private void logError(String method, ClientResponse response) {
            LOGGER.error("{} {}{}", method, webResource, path);
            LOGGER.error("JSON: {}", object);
            LOGGER.error("{}", response.toString());
        }

        private void logError(String method, ClientResponse response, String responseEntity) {
            logError(method, response);
            LOGGER.error("response = {}", responseEntity);
        }

        private <T> T processResponse(ClientResponse response, String method, TypeReference<T> type) {
            try {
                return getValue(response, type);
            } catch (DeserializationException e) {
                if (!LOGGER.isDebugEnabled()) {
                    logError(method, response, e.getResponse());
                }
                throw e;
            }
        }

    }
}