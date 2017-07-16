package project.service.transport;

public enum RESTMethods {

    GET_BOOK("api/v1/bookManagement/book/{id}"); //GET HTTP METHOD

    private final String path;

    /**
     * Constructs an instance of {@link RESTMethods}.
     *
     * @param path A name of an HTTP header.
     */
    RESTMethods(final String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return path;
    }
}