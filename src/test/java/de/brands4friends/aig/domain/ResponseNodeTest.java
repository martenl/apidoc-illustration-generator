package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseNodeTest {

    private ResponseNode instance;
    private final String name = "test-name";
    private final String type = "test-type";
    private List<ResponseElement> children;
    private final boolean required = true;

    @Before
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
        final String expResult = "test-type";
        assertEquals(expResult,instance.getType());
    }

    @Test
    public void testGetRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testGetRequiredWhenFalse() throws Exception {
        instance = new ResponseNode(name,type,children,false);
        assertFalse(instance.isRequired());
    }

    @Test
    public void testToString() throws Exception {
        final String expResult = "test-name : test-type";
        assertEquals(expResult,instance.toString());
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(instance.isRequired());
    }

    @Test
    public void testIsRequiredWhenFalse() throws Exception {
        children = new ArrayList<ResponseElement>();
        instance = new ResponseNode(name,type,children, false);
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
        final ResponseElement childNode = new ResponseNode("childNode","node",Arrays.asList(indirectChild),true);
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