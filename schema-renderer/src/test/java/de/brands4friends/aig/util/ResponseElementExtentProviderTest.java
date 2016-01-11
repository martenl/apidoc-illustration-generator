package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseElement;
import de.brands4friends.aig.domain.ResponseValue;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

public class ResponseElementExtentProviderTest extends EasyMockSupport{

    private ResponseElementExtentProvider instance;
    private final double height = 20;
    private final double delta = .001;
    private FontMetrics mockFontMetrics;

    @Before
    public void setUp() throws Exception {
        mockFontMetrics = mock(FontMetrics.class);
        instance = new ResponseElementExtentProvider((int)height,mockFontMetrics);
    }

    @Test
    public void testGetWidth() throws Exception {
        final ResponseElement element = new ResponseValue("name","type",true);
        final String testString = element.toString();
        final int returnedLength = 5;
        final int expResult = returnedLength + 20;
        expect(mockFontMetrics.stringWidth(testString)).andReturn(returnedLength);
        replayAll();
        assertEquals(expResult,instance.getWidth(element),delta);
        verifyAll();
    }

    @Test
    public void testGetHeight() throws Exception {
        assertEquals(height,instance.getHeight(null),delta);
    }
}