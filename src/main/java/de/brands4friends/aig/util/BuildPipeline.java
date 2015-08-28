package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiDoc;

import java.io.IOException;

public class BuildPipeline {

    private final FileProcessor fileProcessor;
    private final IllustrationGenerator illustrationGenerator;

    public BuildPipeline(FileProcessor fileProcessor, IllustrationGenerator illustrationGenerator) {
        this.fileProcessor = fileProcessor;
        this.illustrationGenerator = illustrationGenerator;
    }

    public void execute(String inputFileName, String outputFileName) throws IOException {
        System.out.println("reading file");
        ApiDoc apiDoc = fileProcessor.readFromFile(inputFileName);
        illustrationGenerator.createIllustration(apiDoc,outputFileName);
    }
}
