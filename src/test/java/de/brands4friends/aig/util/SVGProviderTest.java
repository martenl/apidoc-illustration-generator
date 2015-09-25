package de.brands4friends.aig.util;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertNotNull;

public class SVGProviderTest {

    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private SVGProvider instance;

    @Before
    public void setUp(){
        instance = new SVGProvider();
    }
    @Test
    public void testProvideCanvas() throws Exception {
        Graphics2D canvas = instance.provideCanvas(HEIGHT,WIDTH);
        assertNotNull(canvas);
    }

    @Test
    public void testStoreCanvas() throws Exception {

    }
}