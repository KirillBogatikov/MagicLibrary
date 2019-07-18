package org.kllbff.magic.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7166805606022798320L;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable t) {
        super(t);
    }
}
