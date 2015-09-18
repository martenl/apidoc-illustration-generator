package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseDescription;

import java.io.IOException;

public interface IllustrationGenerator {

    void createIllustration(ResponseDescription responseDescription, String outputFileName, int outerboundX) throws IOException;
}
