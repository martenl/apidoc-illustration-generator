package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SchemaTest {

    private Schema instance;
    private Map<String,ResponseElement> types;
    @Before
    public void setUp() throws Exception {
        types = new HashMap<String, ResponseElement>();
        //instance = Schema.builder().addTypeMap(types).build();
    }

    @Test
    public void testGetTypes() throws Exception {
        //assertEquals(types,instance.getTypes());
    }
}