package project.service.transport.exception;

public final class SerializationException extends RuntimeException {

    private static final long serialVersionUID = -2611961471196797329L;

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable exception) {
        super(message, exception);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}