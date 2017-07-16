package project.service.transport.exception;

public class RestServiceException extends RuntimeException {

    private static final long serialVersionUID = 6674333041996898294L;

    public RestServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}