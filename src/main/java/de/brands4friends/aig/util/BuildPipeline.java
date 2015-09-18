package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseDescription;

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
        ResponseDescription responseDescription = fileProcessor.readFromFile(inputFileName);
        illustrationGenerator.createIllustration(responseDescription,outputFileName,outerboundX);
    }
}
