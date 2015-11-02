package de.brands4friends.aig;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnknownFileFormatExceptionTest {

    @Test
    public void testIsCorrectInitializedWithTxt(){
        UnknownFileFormatException instance = new UnknownFileFormatException("txt");
        final String expResult = "Unknown Format: txt";
        final String msg = instance.getMessage();
        assertEquals(expResult,msg);
    }

    @Test
    public void testIsCorrectInitializedWithSVG(){
        UnknownFileFormatException instance = new UnknownFileFormatException("svg");
        final String expResult = "Unknown Format: svg";
        final String msg = instance.getMessage();
        assertEquals(expResult,msg);
    }

}