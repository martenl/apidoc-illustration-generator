package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.Schema;

import java.io.IOException;

public class BuildPipeline {

    private final FileProcessor fileProcessor;
    private final IllustrationGenerator illustrationGenerator;

    public BuildPipeline(FileProcessor fileProcessor, IllustrationGenerator illustrationGenerator) {
        this.fileProcessor = fileProcessor;
        this.illustrationGenerator = illustrationGenerator;
    }

    public void execute(String inputFileName, String outputFileName, int outerboundX) throws IOException {
        System.out.println("reading file");
        Schema schema = fileProcessor.readFromFile(inputFileName);
        illustrationGenerator.createIllustration(schema,outputFileName,outerboundX);
    }
}
