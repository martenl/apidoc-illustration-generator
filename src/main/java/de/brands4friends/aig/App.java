package de.brands4friends.aig;

import de.brands4friends.aig.util.*;

import java.io.IOException;


public class App {

    static public void main(String[] args) throws IOException {
        App myApp = new App();
        myApp.execute(args[0],args[1],Integer.parseInt(args[2]));
    }

    public void execute(String inputFileName,String outputFileName,int outerboundX) throws IOException {

        System.out.println("input from "+inputFileName);
        System.out.println("create build pipeline");
        FileProcessor fileProcessor = createFileProcessor(inputFileName);
        CanvasProvider canvasProvider = createCanvasProvider(outputFileName);
        BuildPipeline myPipeline = new BuildPipeline(fileProcessor,new CanvasIllustrationGenerator(canvasProvider));
        System.out.println("running pipeline");
        myPipeline.execute(inputFileName, outputFileName,outerboundX);
        System.out.println("done with pipeline");
    }

    private CanvasProvider createCanvasProvider(String outputFileName) {
        if(outputFileName.endsWith("png")){
            return new PngProvider();
        }
        if(outputFileName.endsWith("jpg") || outputFileName.endsWith("jpeg")){
            return new JpegProvider();
        }
        return new SVGProvider();
    }

    private FileProcessor createFileProcessor(String inputFileName) {
        if(inputFileName.endsWith("json")){
            return new JsonProcessor();
        }
        if(inputFileName.endsWith("yaml")){
            return new YamlProcessor();
        }
        return new TextFileProcessor();

    }
}
