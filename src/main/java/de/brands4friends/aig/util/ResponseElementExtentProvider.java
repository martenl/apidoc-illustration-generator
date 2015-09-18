package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseElement;
import org.abego.treelayout.NodeExtentProvider;

import java.awt.*;

public class ResponseElementExtentProvider implements NodeExtentProvider<ResponseElement> {

    private final int height;
    private final FontMetrics fontMetrics;

    public ResponseElementExtentProvider(int height, FontMetrics fontMetrics) {
        this.height = height;
        this.fontMetrics = fontMetrics;
    }

    @Override
    public double getWidth(ResponseElement element) {
        return fontMetrics.stringWidth(element.toString()) +20;
    }

    @Override
    public double getHeight(ResponseElement responseElement) {
        return height;
    }
}
