package de.brands4friends.aig;

public class UnknownFileFormatException extends Exception {

    public UnknownFileFormatException(final String format){
        super("Unknown Format: "+format);
    }
}
