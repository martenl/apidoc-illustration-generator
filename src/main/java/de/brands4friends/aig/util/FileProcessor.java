package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseDescription;

import java.io.IOException;

public interface FileProcessor {

    ResponseDescription readFromFile(String fileName) throws IOException;
}
