package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaValueTest {

    private boolean required = true;
    private SchemaValue instance;

    @Before
    public void setUp() throws Exception {
        instance = new SchemaValue("test-name","test-type", required);
    }

    @Test
    public void testIsNode() throws Exception {
        assertFalse(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {
        List<SchemaElement> expResult = new ArrayList<SchemaElement>();
        assertEquals(expResult,instance.getChildren());
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