package de.brands4friends.aig.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.brands4friends.aig.domain.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JacksonParser {

    private final ObjectMapper mapper;

    public JacksonParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String,SchemaElement> parseFile(final String fileName) throws IOException {
        final JsonNode rootNode = mapper.readTree(new File(fileName));
        final JsonNode definitions = rootNode.get("definitions");
        final Map<String,SchemaElement> parsedResponseElements = parseDefinitions(definitions);
        if(isObject(rootNode)){
            SchemaElement response = parse("Response", rootNode, true);
            parsedResponseElements.put("Response",response);
        }
        return parsedResponseElements;
    }

    private Map<String,SchemaElement> parseDefinitions(JsonNode definitions){
        final Map<String,SchemaElement> definitionMap = new HashMap<String, SchemaElement>();
        if(definitions == null){
            return definitionMap;
        }
        Iterator<String> fieldNames = definitions.fieldNames();
        while(fieldNames.hasNext()) {
            final String definition = fieldNames.next();
            final SchemaElement response = parse(definition, definitions.get(definition),true);
            definitionMap.put(definition, response);
        }
        return definitionMap;
    }

    private SchemaElement parse(String nodeName, JsonNode node, boolean required){
        if(isObject(node)){
            return parseObject(nodeName,node,required);
        }
        if(isEnum(node)){
            return parseEnum(nodeName,node,required);
        }
        if(isDefined(node)){
            return parseDefined(nodeName,node,required);
        }
        if(isArray(node)){
            return parseArray(nodeName,node,required);
        }
        return parseValue(nodeName,node,required);
    }

    private boolean isObject(JsonNode node){
        JsonNode type = node.get("type");
        if(type != null && type.asText().equals("object")){
            return true;
        }
        return false;
    }

    private SchemaElement parseObject(String objectName,JsonNode node,boolean required){
        final List<SchemaElement> children = new ArrayList<SchemaElement>();
        final JsonNode properties = node.get("properties");
        final List<String> requiredFields = new ArrayList<String>();
        JsonNode requiredFieldsNode = node.get("required");
        if(requiredFieldsNode != null){
            Iterator<JsonNode> iterator = requiredFieldsNode.iterator();
            while(iterator.hasNext()){
                requiredFields.add(iterator.next().asText());
            }
        }
        if(properties != null){
            children.addAll(parseProperties(properties,requiredFields));
        }
        final JsonNode patternProperties = node.get("patternProperties");
        if(patternProperties != null){
            children.addAll(parsePatternProperties(patternProperties));
        }
        return new SchemaNode(objectName,"object",children,required);
    }

    private List<SchemaElement> parseProperties(JsonNode properties,List<String> requiredFields){
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        final Iterator<String> fieldNames = properties.fieldNames();
        while(fieldNames.hasNext()) {
            final String propertyName = fieldNames.next();
            final boolean isRequiredField = requiredFields.contains(propertyName);
            JsonNode property = properties.get(propertyName);
            children.add(parse(propertyName,property,isRequiredField));
        }
        return children;
    }

    private List<SchemaElement> parsePatternProperties(JsonNode patternProperties){
        List<SchemaElement> children = new ArrayList<SchemaElement>();
        final Iterator<String> patterns = patternProperties.fieldNames();
        while(patterns.hasNext()){
            final String pattern = patterns.next();
            JsonNode property = patternProperties.get(pattern);
            children.add(parse("Pattern: "+pattern,property,true));
        }
        return children;
    }

    private boolean isEnum(JsonNode node){
        return node.get("enum") != null;
    }

    private SchemaElement parseEnum(String nodeName, JsonNode node, boolean required) {
        JsonNode values = node.get("enum");
        List<String> enumValues = new ArrayList<String>();
        Iterator<JsonNode> elements = values.elements();
        while (elements.hasNext()){
            final String element = elements.next().asText();
            enumValues.add(element);
        }
        return new SchemaEnum(nodeName,enumValues);
    }

    private boolean isArray(JsonNode node){
        JsonNode type = node.get("type");
        return type != null && type.asText().equals("array");
    }

    private SchemaElement parseArray(String nodeName, JsonNode node, boolean required) {
        final List<SchemaElement> arrayValues = new ArrayList<SchemaElement>();
        final String elementName;
        if(node.hasNonNull("id")){
            elementName = node.get("id").asText();
        }else{
            elementName = "element";
        }
        arrayValues.add(parse(elementName,node.get("items"), true));
        JsonNode minItemsNode = node.get("minItems");
        String minItems = "0";
        if(minItemsNode != null){
            minItems = minItemsNode.asText();
        }
        String maxItems = "n";
        JsonNode maxItemsNode = node.get("maxItems");
        if(maxItemsNode != null){
            maxItems = maxItemsNode.asText();
        }
        return new SchemaArray(nodeName,arrayValues,minItems,maxItems,required);
    }

    private boolean isDefined(JsonNode node){
        JsonNode reference = node.get("$ref");
        if(reference != null && reference.asText().contains("#/definitions/")){
            return true;
        }
        return false;
    }

    private SchemaElement parseDefined(String name, JsonNode node, boolean required){
        String reference = node.get("$ref").asText();
        String propertyType = reference.replace("#/definitions/","");
        return new SchemaReference(name,propertyType, reference, required);
    }

    private SchemaElement parseValue(String valueName,JsonNode node,boolean required){
        JsonNode type = node.get("type");
        if(type != null){
            return new SchemaValue(valueName,type.asText(),required);
        }
        return new SchemaValue(valueName,"unknownType",required);
    }
}
