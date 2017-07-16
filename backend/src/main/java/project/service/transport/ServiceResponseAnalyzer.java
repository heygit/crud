package project.service.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.service.transport.exception.DeserializationException;
import project.service.transport.exception.InternalServerErrorException;
import project.service.transport.exception.RestServiceException;
import project.service.transport.exception.model.TransportErrorInfo;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ServiceResponseAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResponseAnalyzer.class);

    private final ObjectMapper serializer;

    public ServiceResponseAnalyzer() {
        this(new ObjectMapper());
    }

    public ServiceResponseAnalyzer(ObjectMapper serializer) {
        this.serializer = serializer;
    }

    public <T extends TransportErrorInfo> T parseErrorResponse(final String response, final Class<T> errorClass) {
        if (response == null) {
            return null;
        }
        try {
            return serializer.readValue(response, errorClass);
        } catch (Exception e) {
            throw new DeserializationException(
                    "Error response " + response
                            + " can't be deserialized to class " + errorClass.getName(), e, response);
        }
    }

    public RuntimeException analyzeErrorResponse(int status, final String response, Exception source) {
        if (response == null) {
            return null;
        }

        switch (status) {
            case 500:
                TransportErrorInfo errorInfo = parseErrorResponse(response, TransportErrorInfo.class);
                return new InternalServerErrorException(errorInfo);

            default:
                return new DeserializationException("Error while parsing the response from service", source, response);
        }
    }

    public <T> T parseResponse(final InputStream responseStream,
                               final int status, TypeReference<T> type,
                               final String encoding) {
        if (status == JAXRSClientService.NO_CONTENT) {
            return null;
        }

        String response = null;
        try {
            if (encoding != null && !encoding.isEmpty()) {
                response = IOUtils.toString(responseStream, encoding);
            } else {
                response = IOUtils.toString(responseStream);
            }

            if ((status == JAXRSClientService.ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                LOGGER.error("response = {}", response);
            } else {
                LOGGER.debug("response = {}", response);
            }

            if (response.length() > 0) {
                return serializer.readValue(response, type);
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw analyzeErrorResponse(status, response, e);
        } catch (IOException e) {
            throw new RestServiceException("Error while reading the response from service", e);
        }
    }

    public String parseResponseAsString(final InputStream responseStream, final int status, final String encoding) {
        if (status == JAXRSClientService.NO_CONTENT) {
            return null;
        }

        String response = null;
        try {
            if (encoding != null && !encoding.isEmpty()) {
                response = IOUtils.toString(responseStream, encoding);
            } else {
                response = IOUtils.toString(responseStream);
            }

            if ((status == JAXRSClientService.ERROR_STATUS) && (!LOGGER.isDebugEnabled())) {
                LOGGER.error("response = {}", response);
            } else {
                LOGGER.debug("response = {}", response);
            }

            return response;
        } catch (JsonProcessingException e) {
            throw analyzeErrorResponse(status, response, e);
        } catch (IOException e) {
            throw new RestServiceException("Error while reading the response from service", e);
        }
    }
}