package de.brands4friends.aig.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.brands4friends.aig.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class JacksonParserTest {

    private JacksonParser instance;
    private File testFile;

    @Before
    public void setUp(){
        instance = new JacksonParser(new ObjectMapper());
    }

    @Test
    public void testParseEmptyFile() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        testFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("{\"definitions\":{}}");
        writer.flush();
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithEmptyResponse() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        SchemaElement response = new SchemaNode("Response","object",new ArrayList<SchemaElement>(),true);
        expResult.put("Response",response);
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        testFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("{\"type\":\"object\",\"properties\":{},\"definitions\":{}}");
        writer.flush();
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredInteger() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaValue("my-integer","integer",true));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        testFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("{\"type\":\"object\",\"properties\":{\"my-integer\":{\"type\":\"integer\"}},\"required\":[\"my-integer\"],\"definitions\":{}}");
        writer.flush();
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithNotRequiredInteger() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaValue("my-integer","integer",false));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        testFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("{\"type\":\"object\",\"properties\":{\"my-integer\":{\"type\":\"integer\"}},\"definitions\":{}}");
        writer.flush();
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredEnum() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaEnum("my-enum", Arrays.asList("on","off")));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        testFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(testFile);
        writer.write("{\"type\":\"object\",\"properties\":{\"my-enum\": {\"enum\": [\"on\",\"off\"]}},\"required\":[\"my-enum\"],\"definitions\":{}}");
        writer.flush();
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithNotRequiredEnum() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaEnum("my-enum", Arrays.asList("on", "off")));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-enum\": {\"enum\": [\"on\",\"off\"]}},\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredArray() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"0","n",true));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}},\"required\":[\"my-array\"],\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }


    @Test
    public void testParseWithResponseWithNotRequiredArray() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"0","n",false));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response", response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}},\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredArrayWithMax() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"0","1",true));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"maxItems\":1,\"items\":{\"type\":\"integer\"}}},\"required\":[\"my-array\"],\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithNotRequiredArrayWithMax() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"0","1",false));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"maxItems\":1,\"items\":{\"type\":\"integer\"}}},\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredArrayWithMin() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"1","n",true));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"minItems\":1,\"items\":{\"type\":\"integer\"}}},\"required\":[\"my-array\"],\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithNotRequiredArrayWithMin() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        List<SchemaElement> arrayItems = new ArrayList<SchemaElement>();
        arrayItems.add(new SchemaValue("element", "integer", true));
        children.add(new SchemaArray("my-array",arrayItems,"1","n",false));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response",response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-array\":{\"type\":\"array\",\"minItems\":1,\"items\":{\"type\":\"integer\"}}},\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithRequiredReference() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaReference("my-ref","my-ref","#/definitions/my-ref",true));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response", response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-ref\": {\"$ref\": " +
                "\"#/definitions/my-ref\" }},\"required\":[\"my-ref\"],\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    @Test
    public void testParseWithResponseWithNotRequiredReference() throws Exception {
        Map<String,SchemaElement> expResult = new HashMap<String, SchemaElement>();
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        children.add(new SchemaReference("my-ref", "my-ref", "#/definitions/my-ref", false));
        SchemaElement response = new SchemaNode("Response","object",children,true);
        expResult.put("Response", response);
        testFile = createTmpJsonFile("{\"type\":\"object\",\"properties\":{\"my-ref\": {\"$ref\": \"#/definitions/my-ref\" }},\"definitions\":{}}");
        Map<String,SchemaElement> result = instance.parseFile(testFile.getAbsolutePath());
        assertEquals(expResult,result);
    }

    private File createTmpJsonFile(final String json) throws Exception {
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        File tmpFile = temporaryFolder.newFile();
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(json);
        writer.flush();
        return tmpFile;
    }

}