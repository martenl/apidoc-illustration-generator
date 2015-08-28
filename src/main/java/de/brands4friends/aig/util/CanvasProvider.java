package de.brands4friends.aig.util;

import java.awt.*;
import java.io.IOException;

public interface CanvasProvider {

    Graphics2D provideCanvas(int height,int width);

    void storeCanvas(Graphics2D canvas, String fileName) throws IOException;

}
