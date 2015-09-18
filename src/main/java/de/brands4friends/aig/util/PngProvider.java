package de.brands4friends.aig.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PngProvider implements CanvasProvider {

    Map<Graphics2D,BufferedImage> images = new HashMap<Graphics2D, BufferedImage>();

    @Override
    public Graphics2D provideCanvas(int height, int width) {
        BufferedImage canvasImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D canvas = canvasImage.createGraphics();
        canvas.setBackground(Color.WHITE);
        images.put(canvas,canvasImage);
        return canvas;
    }

    @Override
    public void storeCanvas(Graphics2D canvas, String fileName, int maxWidth, int offsetY) throws IOException {
        BufferedImage canvasImage = images.get(canvas);
        ImageIO.write(canvasImage, "png", new File(fileName));
    }
}
