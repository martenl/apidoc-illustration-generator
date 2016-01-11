package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseElement;
import de.brands4friends.aig.domain.ResponseReference;
import de.brands4friends.aig.domain.ResponseValue;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.easymock.EasyMock.expect;

public class CanvasTest extends EasyMockSupport {

    private static final double X = 100;
    private static final double Y = 100;
    private static final double HEIGHT = 100;
    private static final double WIDTH = 100;

    private static final int FONT_HEIGHT = 10;
    Canvas instance;
    Graphics2D mockGraphics;

    Rectangle2D.Double box;

    @Before
    public void setUp() throws Exception {
        mockGraphics = createNiceMock(Graphics2D.class);
        instance = new Canvas(mockGraphics);

        box = new Rectangle2D.Double(X,Y,HEIGHT,WIDTH);
    }

    //@Test
    public void testDrawVertexWithValue() throws Exception {
        ResponseValue responseValue = new ResponseValue("my-value","my-type",true);
        Font mockFont = createMock(Font.class);
        Color mockColor = createNiceMock(Color.class);
        expect(mockFont.getSize()).andReturn(FONT_HEIGHT);
        expect(mockGraphics.getFont()).andReturn(mockFont);
        //expect(mockGraphics.getColor()).andReturn(mockColor);
        //mockGraphics.setColor(Canvas.CAPTION_COLOR);
        final int drawX = (int)X+ FONT_HEIGHT / 2 + 2;
        final int drawY = (int) box.y + FONT_HEIGHT + 1;
        //mockGraphics.drawString("my-value : my-type",drawX, drawY);
        mockGraphics.draw(box);
        //mockGraphics.setColor(mockColor);
        replayAll();
        instance.drawVertex(responseValue,box);
        verifyAll();
    }

    //@Test
    public void testDrawVertexWithUnrequiredValue() throws Exception {
        ResponseValue responseValue = new ResponseValue("my-value","my-type",false);
        instance.drawVertex(responseValue,box);
    }


    //@Test
    public void testDrawVertexWithReference() throws Exception {
        ResponseElement element = new ResponseReference("my-ref","my-type","#/definitions/my-type",true);
        Font mockFont = createMock(Font.class);
        Color mockColor = createNiceMock(Color.class);
        expect(mockFont.getSize()).andReturn(FONT_HEIGHT);
        expect(mockGraphics.getFont()).andReturn(mockFont);
        expect(mockGraphics.getColor()).andReturn(mockColor);

        replayAll();
        instance.drawVertex(element,box);
        verifyAll();
    }

    //@Test
    public void testDrawVertexWithUnrequiredReference() throws Exception {
        ResponseElement responseValue = new ResponseReference("my-ref","my-type","#/definitions/my-type",false);

    }
    @Test
    public void testDrawEdgesToChildren() throws Exception {

    }

    @Test
    public void testGetCanvasContent() throws Exception {

    }
}