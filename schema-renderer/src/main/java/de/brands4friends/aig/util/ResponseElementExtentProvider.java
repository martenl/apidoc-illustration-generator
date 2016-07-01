package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.SchemaElement;
import org.abego.treelayout.NodeExtentProvider;

import java.awt.*;

public class ResponseElementExtentProvider implements NodeExtentProvider<SchemaElement> {

    private final int height;
    private final FontMetrics fontMetrics;

    public ResponseElementExtentProvider(int height, FontMetrics fontMetrics) {
        this.height = height;
        this.fontMetrics = fontMetrics;
    }

    @Override
    public double getWidth(SchemaElement element) {
        return fontMetrics.stringWidth(element.toString()) +20;
    }

    @Override
    public double getHeight(SchemaElement schemaElement) {
        return height;
    }
}
