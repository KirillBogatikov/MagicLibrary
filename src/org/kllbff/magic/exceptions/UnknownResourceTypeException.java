package org.kllbff.magic.exceptions;

public class UnknownResourceTypeException extends Exception {
    static final long serialVersionUID = 8531825699361561100L;

    public UnknownResourceTypeException(String path) {
        super("Failed to parse values file \"" + path + "\": Unknown values resource type");
    }

}
