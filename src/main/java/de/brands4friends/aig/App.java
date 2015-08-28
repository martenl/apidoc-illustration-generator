package de.brands4friends.aig;

import de.brands4friends.aig.util.BuildPipeline;
import de.brands4friends.aig.util.CanvasIllustrationGenerator;
import de.brands4friends.aig.util.JpegProvider;
import de.brands4friends.aig.util.TextFileProcessor;

import java.io.IOException;

public class App {
    
    static public void main(String[] args) throws IOException {
        App myApp = new App();
        myApp.execute(args[0],args[1]);
    }

    public void execute(String inputFileName,String outputFileName) throws IOException {
        System.out.println("create build pipeline");
        BuildPipeline myPipeline = new BuildPipeline(new TextFileProcessor(),new CanvasIllustrationGenerator(new JpegProvider()));
        System.out.println("running pipeline");
        myPipeline.execute(inputFileName, outputFileName);
        System.out.println("done with pipeline");
    }
}
