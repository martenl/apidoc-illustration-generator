package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseReferenceTest {

    private ResponseReference instance;
    private final String name = "test-name";
    private final String type = "test-type";
    private final String reference = "test-reference";

    @Before
    public void setUp() throws Exception {
        instance = new ResponseReference(name,type,reference,true);
    }

    @Test
    public void testIsNode() throws Exception {
        assertFalse(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {
        List<ResponseElement> expResult = new ArrayList<ResponseElement>();
        assertEquals(expResult,instance.getChildren());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(name,instance.getName());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(type,instance.getType());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testIsAncestor() throws Exception {
        assertFalse(instance.isAncestor(instance));
    }

    @Test
    public void testToString() throws Exception {
        final String expResult = name+" : "+type;
        assertEquals(expResult,instance.toString());
    }

    @Test
    public void testGetReference() throws Exception {
        assertEquals(reference,instance.getReference());
    }
}