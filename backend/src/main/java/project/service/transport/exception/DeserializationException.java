package project.service.transport.exception;

public class DeserializationException  extends RuntimeException {

    private static final long serialVersionUID = -2611961471196797359L;

    private String response;

    public String getResponse() {
        return response;
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, String response) {
        super(message);
        this.response = response;
    }

    public DeserializationException(String message, Throwable exception, String response) {
        super(message, exception);
        this.response = response;
    }

    public DeserializationException(String message, Throwable exception) {
        super(message, exception);
    }
}