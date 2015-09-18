package de.brands4friends.aig.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseNodeTest {

    private ResponseNode instance;
    private final String name = "test-name";
    private final String type = "test-type";
    private List<ResponseElement> children;
    private final boolean required = true;

    @org.junit.Before
    public void setUp() throws Exception {
        children = new ArrayList<ResponseElement>();
        instance = new ResponseNode(name,type,children, required);
    }

    @Test
    public void testIsNode() throws Exception {
        assertTrue(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {
        final String expResult = "test-name";
        assertEquals(expResult,instance.getName());
    }

    @Test
    public void testGetType() throws Exception {
        final String expResult = "test-type";
        assertEquals(expResult,instance.getType());
    }

    @Test
    public void testGetRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testGetRequired2() throws Exception {
        instance = new ResponseNode(name,type,children,false);
        assertFalse(instance.isRequired());
    }

    @Test
    public void testToString() throws Exception {
        final String expResult = "{ test-name:test-type\n}";
        assertEquals(expResult,instance.toString());
    }
}