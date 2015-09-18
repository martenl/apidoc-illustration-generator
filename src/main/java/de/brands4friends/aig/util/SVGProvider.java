package de.brands4friends.aig.util;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.IOException;

public class SVGProvider implements CanvasProvider {

    @Override
    public Graphics2D provideCanvas(int height, int width) {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        return svgGenerator;
    }

    @Override
    public void storeCanvas(Graphics2D canvas, String fileName, int width, int height) throws IOException{
        if(canvas instanceof SVGGraphics2D){
            SVGGraphics2D svgCanvas = (SVGGraphics2D) canvas;
            svgCanvas.setSVGCanvasSize(new Dimension(height,width));
            svgCanvas.stream(fileName);
        }
    }
}
