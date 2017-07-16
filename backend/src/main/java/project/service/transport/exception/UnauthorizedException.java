package project.service.transport.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable exception) {
        super(message, exception);
    }
}