package de.brands4friends.aig.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ResponseDescriptionTest {

    private ResponseDescription instance;
    private Map<String,ResponseElement> types;
    @Before
    public void setUp() throws Exception {
        types = new HashMap<String, ResponseElement>();
        instance = ResponseDescription.builder().addTypeMap(types).build();
    }

    @Test
    public void testGetTypes() throws Exception {
        assertEquals(types,instance.getTypes());
    }
}