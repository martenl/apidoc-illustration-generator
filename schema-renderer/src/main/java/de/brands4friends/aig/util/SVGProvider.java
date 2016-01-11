package de.brands4friends.aig.util;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.IOException;

public class SVGProvider implements CanvasProvider {

    @Override
    public de.brands4friends.aig.util.Canvas provideCanvas(int height, int width) {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        return new Canvas(svgGenerator);
    }

    @Override
    public void storeCanvas(Canvas canvas, String fileName, int width, int height) throws IOException{
        Graphics2D canvasContent = canvas.getCanvasContent();
        if(canvasContent instanceof SVGGraphics2D){
            SVGGraphics2D svgCanvas = (SVGGraphics2D) canvasContent;
            svgCanvas.setSVGCanvasSize(new Dimension(height,width));
            svgCanvas.stream(fileName);
        }
    }
}
