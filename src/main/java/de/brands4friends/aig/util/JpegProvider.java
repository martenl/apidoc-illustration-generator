package de.brands4friends.aig.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JpegProvider implements CanvasProvider {

    Map<Graphics2D,BufferedImage> images = new HashMap<Graphics2D, BufferedImage>();

    @Override
    public Graphics2D provideCanvas(int height, int width) {
        BufferedImage canvasImage = new BufferedImage(width,height,1);
        Graphics2D canvas = canvasImage.createGraphics();
        images.put(canvas,canvasImage);
        return canvas;
    }

    @Override
    public void storeCanvas(Graphics2D canvas, String fileName) throws IOException {
        BufferedImage canvasImage = images.get(canvas);
        ImageIO.write(canvasImage,"jpg",new File(fileName));
    }
}
