package de.brands4friends.aig.util;

import java.io.IOException;

public interface CanvasProvider {

    Canvas provideCanvas(int height,int width);

    void storeCanvas(Canvas canvas, String fileName, int maxWidth, int offsetY) throws IOException;

}
