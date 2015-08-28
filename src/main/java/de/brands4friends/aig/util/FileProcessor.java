package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiDoc;

import java.io.IOException;

public interface FileProcessor {

    ApiDoc readFromFile(String fileName) throws IOException;
}
