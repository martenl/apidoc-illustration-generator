package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.Schema;

import java.io.IOException;

public interface IllustrationGenerator {

    void createIllustration(Schema schema, String outputFileName, int outerboundX) throws IOException;
}
