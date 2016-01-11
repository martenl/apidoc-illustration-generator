package de.brands4friends.aig.parser;

import de.brands4friends.aig.domain.Schema;

import java.io.IOException;

public interface FileProcessor {

    Schema readFromFile(String fileName) throws IOException;
}
