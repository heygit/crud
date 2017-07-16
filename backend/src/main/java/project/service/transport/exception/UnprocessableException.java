package project.service.transport.exception;


import project.service.transport.exception.model.TransportErrorInfo;

public class UnprocessableException extends RuntimeException {

    private static final long serialVersionUID = 8203631794703888248L;

    private final TransportErrorInfo error;

    public UnprocessableException(TransportErrorInfo error) {
        super(error.getUserMessage(), null);
        this.error = error;
    }

    public TransportErrorInfo getError() {
        return error;
    }
}