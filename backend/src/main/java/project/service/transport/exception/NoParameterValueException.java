package project.service.transport.exception;

public class NoParameterValueException extends RuntimeException {

    private static final long serialVersionUID = -3569989923556090260L;

    public NoParameterValueException(String message) {
        super(message);
    }

    public NoParameterValueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}