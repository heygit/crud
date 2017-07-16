package project.service.transport;

import com.fasterxml.jackson.core.type.TypeReference;
import project.service.transport.DetailedResponse;
import project.service.transport.RESTRequestParameters;

import java.util.concurrent.Future;

public interface JAXRSClientService {
    int ERROR_STATUS = 422;
    int NO_CONTENT = 204;

    /**
     * Calls GET method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> T get(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
              TypeReference<T> responseType);

    byte[] getFile(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters);

    /**
     * Calls GET method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> DetailedResponse<T> getDetailed(RESTResources.RESTResource resource, RESTMethods method,
                                        RESTRequestParameters parameters, TypeReference<T> responseType);

    /**
     * Asynchronously calls GET method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> Future<T> asyncGet(RESTResources.RESTResource resource, RESTMethods method,
                           RESTRequestParameters parameters, TypeReference<T> responseType);

    /**
     * Calls PUT method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> T put(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
              Object data, TypeReference<T> responseType);

    /**
     * Asynchronously calls PUT method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> Future<T> asyncPut(RESTResources.RESTResource resource, RESTMethods method,
                           RESTRequestParameters parameters, Object data, TypeReference<T> responseType);

    /**
     * Calls POST method to REST API
     *
     * @param resource
     * @param method
     * @param parameters
     * @param data
     * @param responseType
     * @param <T>
     * @return
     */
    <T> T post(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
               Object data, TypeReference<T> responseType);

    /**
     * Calls POST method to REST API
     *
     * @param resource     REST API server
     * @param method       REST method
     * @param parameters   REST request parameters
     * @param responseType response object type
     * @param <T>
     * @return
     */
    <T> DetailedResponse<T> postDetailed(RESTResources.RESTResource resource, RESTMethods method,
                                         RESTRequestParameters parameters, Object data, TypeReference<T> responseType);

    /**
     * Asynchronously calls POST method to REST API
     *
     * @param resource
     * @param method
     * @param parameters
     * @param data
     * @param responseType
     * @param <T>
     * @return
     */
    <T> Future<T> asyncPost(RESTResources.RESTResource resource, RESTMethods method,
                            RESTRequestParameters parameters, Object data, TypeReference<T> responseType);

    /**
     * Calls DETELE method to REST API
     *
     * @param resource
     * @param method
     * @param parameters
     * @param data
     * @param responseType
     * @param <T>
     * @return
     */
    <T> T delete(RESTResources.RESTResource resource, RESTMethods method, RESTRequestParameters parameters,
                 Object data, TypeReference<T> responseType);
}