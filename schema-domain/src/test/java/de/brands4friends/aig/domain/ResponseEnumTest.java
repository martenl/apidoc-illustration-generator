package de.brands4friends.aig.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseEnumTest {

    private ResponseEnum instance;
    private List<String> values;

    @org.junit.Before
    public void setUp() throws Exception {
        values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        instance = new ResponseEnum("test-name",values);
    }

    @Test
    public void testIsNode() throws Exception {
        assertFalse(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {
        final List<ResponseElement> expResult = new ArrayList<ResponseElement>();
        assertEquals(expResult,instance.getChildren());
    }

    @Test
    public void testGetName() throws Exception {
        final String expResult = "test-name";
        assertEquals(expResult,instance.getName());
    }

    @Test
    public void testGetType() throws Exception {
        final String expResult = "[value1 | value2]";
        assertEquals(expResult,instance.getType());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testIsAncestor() throws Exception {
        final ResponseElement notAChild = new ResponseValue("notAtChild","string",false);
        assertFalse(instance.isAncestor(notAChild));
    }

    @Test
    public void testToString() throws Exception {
        final String expResult = "test-name : [value1|value2]";
        assertEquals(expResult,instance.toString());
    }
}