package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaArrayTest {

    private final String NAME = "test-name";
    private final String MIN = "0";
    private final String MAX = "n";
    private final boolean required = true;
    private List<SchemaElement> children;
    private SchemaArray instance;

    @Before
    public void setUp() throws Exception {
        children = new ArrayList<SchemaElement>();
        instance = new SchemaArray(NAME,children,MIN,MAX, required);
    }

    @Test
    public void testIsNode() throws Exception {
        assertTrue(instance.isNode());
    }

    @Test
    public void testGetChildren() throws Exception {
        final List<SchemaElement> expResult = new ArrayList<SchemaElement>();
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
        instance = new SchemaArray(NAME,new ArrayList<SchemaElement>(),MIN,MAX, false);
        assertFalse(instance.isRequired());
    }

    @Test
    public void testIsAncestor() throws Exception {
        final SchemaElement notAChild = new SchemaValue("notAChild","string",false);
        assertFalse(instance.isAncestor(notAChild));
    }

    @Test
    public void testIsAncestorWithDirectChild() throws Exception {
        final SchemaElement child = new SchemaValue("child","string",false);
        children.add(child);
        assertTrue(instance.isAncestor(child));
    }

    @Test
    public void testIsAncestorWithIndirectChild() throws Exception {
        final SchemaElement indirectChild = new SchemaValue("indirectChild","string",false);
        final SchemaElement childNode = new SchemaNode("childNode","node", Arrays.asList(indirectChild),true);
        children.add(childNode);
        assertTrue(instance.isAncestor(indirectChild));
    }

    @Test
    public void testIsAncestorWithIndirectNotAChild() throws Exception {
        final SchemaElement indirectNotAChild = new SchemaValue("indirectNotAChild","string",false);
        final SchemaElement indirectChild = new SchemaValue("indirectChild","string",false);
        final SchemaElement childNode = new SchemaNode("childNode","node",Arrays.asList(indirectChild),true);
        children.add(childNode);
        assertFalse(instance.isAncestor(indirectNotAChild));
    }
}