package org.kllbff.magic.exceptions;

public class IncorrectColorException extends ParsingException {
    private static final long serialVersionUID = 7295041344810365374L;

    public static final String INCORRECT_DEPTH = "Incorrect depth of color: %d, supported only 3 and 4 bytes";
    public static final String INCORRECT_BYTES_LENGTH = "Cannot parse color from %d bytes";
    
    public IncorrectColorException(String message, Object... params) {
        super(String.format(message, params));
    }
}
