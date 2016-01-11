package de.brands4friends.aig.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseValueTest {

    private boolean required = true;
    private ResponseValue instance;
    @org.junit.Before
    public void setUp() throws Exception {
        instance = new ResponseValue("test-name","test-type", required);
    }

    @org.junit.Test
    public void testIsNode() throws Exception {
        assertFalse(instance.isNode());
    }

    @org.junit.Test
    public void testGetChildren() throws Exception {
        List<ResponseElement> expResult = new ArrayList<ResponseElement>();
        assertEquals(expResult,instance.getChildren());
    }

    @org.junit.Test
    public void testGetName() throws Exception {
        final String expResult = "test-name";
        assertEquals(expResult,instance.getName());
    }

    @org.junit.Test
    public void testGetType() throws Exception {
        final String expResult = "test-type";
        assertEquals(expResult,instance.getType());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        final String expResult = "test-name:test-type";
        assertEquals(expResult,instance.toString());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testIsAncestor() throws Exception {
        assertFalse(instance.isAncestor(instance));
    }
}