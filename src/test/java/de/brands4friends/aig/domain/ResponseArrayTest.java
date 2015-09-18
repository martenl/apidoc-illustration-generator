package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseArrayTest {

    private final String NAME = "test-name";
    private final String MIN = "0";
    private final String MAX = "n";
    private final boolean required = true;
    private ResponseArray instance;

    @Before
    public void setUp() throws Exception {
        instance = new ResponseArray(NAME,new ArrayList<ResponseElement>(),MIN,MAX, required);
    }

    @Test
    public void testIsNode() throws Exception {
        assertTrue(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {
        final List<ResponseElement> expResult = new ArrayList<ResponseElement>();
        assertEquals(expResult,instance.getChildren());
    }

    @Test
    public void testGetName() throws Exception {
        final String expResult = NAME;
        assertEquals(expResult,instance.getName());
    }

    @Test
    public void testGetType() throws Exception {
        final String expResult = "array";
        assertEquals(expResult,instance.getType());
    }

    @Test
    public void testGetMin() throws Exception {
        final String expResult = MIN;
        assertEquals(expResult,instance.getMin());
    }

    @Test
    public void testGetMax() throws Exception {
        final String expResult = MAX;
        assertEquals(expResult,instance.getMax());
    }

    @Test
    public void testToString() throws Exception {
        final String expResult = "[]";
        assertEquals(expResult,instance.toString());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testIsRequiredWithFalse() throws Exception {
        instance = new ResponseArray(NAME,new ArrayList<ResponseElement>(),MIN,MAX, false);
        assertFalse(instance.isRequired());
    }
}