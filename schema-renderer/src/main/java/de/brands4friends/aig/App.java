package de.brands4friends.aig;

import de.brands4friends.aig.parser.FileProcessor;
import de.brands4friends.aig.parser.SchemaParser;
import de.brands4friends.aig.util.BuildPipeline;
import de.brands4friends.aig.util.CanvasIllustrationGenerator;
import de.brands4friends.aig.util.CanvasProvider;
import de.brands4friends.aig.util.SVGProvider;

import java.io.IOException;


public class App {

    static public void main(String[] args) throws UnknownFileFormatException,IOException {
        App myApp = new App();
        myApp.execute(args[0],args[1],Integer.parseInt(args[2]));
    }

    public void execute(String inputFileName,String outputFileName,int outerboundX) throws UnknownFileFormatException,IOException {

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
        return new SVGProvider();
    }

    private FileProcessor createFileProcessor(String inputFileName) throws UnknownFileFormatException{
        final String format = inputFileName.substring(inputFileName.lastIndexOf(".")+1);
        if("json".equals(format)){
            return SchemaParser.jsonParser();
        }
        if("yaml".equals(format)){
            return SchemaParser.yamlParser();
        }
        throw new UnknownFileFormatException(format);

    }
}
