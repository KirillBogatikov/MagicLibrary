package org.kllbff.magic.exceptions;

public class ParsingException extends RuntimeException {
    private static final long serialVersionUID = 2622048090225222741L;

    public ParsingException() {}

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(Throwable t) {
        super(t);
    }
}
