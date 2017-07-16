package project.service.transport.exception;

public class ServiceUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 3385097102454297037L;

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable exception) {
        super(message, exception);
    }
}