package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class SchemaTest {

    private Schema instance;
    private Map<String,SchemaElement> types;
    @Before
    public void setUp() throws Exception {
        types = new HashMap<String, SchemaElement>();
        instance = Schema.builder().addTypeMap(types).build();
    }

    @Test
    public void testGetTypes() throws Exception {
        assertEquals(types,instance.getTypes());
    }
}