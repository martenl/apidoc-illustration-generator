package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiDoc;

import java.io.IOException;

public interface IllustrationGenerator {

    void createIllustration(ApiDoc apiDoc,String outputFileName) throws IOException;
}
