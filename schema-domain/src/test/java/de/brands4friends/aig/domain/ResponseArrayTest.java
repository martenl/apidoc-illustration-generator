package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseArrayTest {

    private final String NAME = "test-name";
    private final String MIN = "0";
    private final String MAX = "n";
    private final boolean required = true;
    private List<ResponseElement> children;
    private ResponseArray instance;

    @Before
    public void setUp() throws Exception {
        children = new ArrayList<ResponseElement>();
        instance = new ResponseArray(NAME,children,MIN,MAX, required);
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
        assertEquals(expResult, instance.getType());
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
        final String expResult = NAME +" : array["+MIN+" : "+MAX+"]";
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

    @Test
    public void testIsAncestor() throws Exception {
        final ResponseElement notAChild = new ResponseValue("notAChild","string",false);
        assertFalse(instance.isAncestor(notAChild));
    }

    @Test
    public void testIsAncestorWithDirectChild() throws Exception {
        final ResponseElement child = new ResponseValue("child","string",false);
        children.add(child);
        assertTrue(instance.isAncestor(child));
    }

    @Test
    public void testIsAncestorWithIndirectChild() throws Exception {
        final ResponseElement indirectChild = new ResponseValue("indirectChild","string",false);
        final ResponseElement childNode = new ResponseNode("childNode","node", Arrays.asList(indirectChild),true);
        children.add(childNode);
        assertTrue(instance.isAncestor(indirectChild));
    }

    @Test
    public void testIsAncestorWithIndirectNotAChild() throws Exception {
        final ResponseElement indirectNotAChild = new ResponseValue("indirectNotAChild","string",false);
        final ResponseElement indirectChild = new ResponseValue("indirectChild","string",false);
        final ResponseElement childNode = new ResponseNode("childNode","node",Arrays.asList(indirectChild),true);
        children.add(childNode);
        assertFalse(instance.isAncestor(indirectNotAChild));
    }
}