package project.service.transport.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3569989923556090259L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}