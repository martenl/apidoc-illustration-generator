package de.brands4friends.aig.util;

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

    public Map<String,ResponseElement> parseFile(final String fileName) throws IOException {
        final JsonNode rootNode = mapper.readTree(new File(fileName));
        final JsonNode definitions = rootNode.get("definitions");
        final Map<String,ResponseElement> parsedResponseElements = parseDefinitions(definitions);
        if(isObject(rootNode)){
            ResponseElement response = parse("Response", rootNode, true);
            parsedResponseElements.put("Response",response);
        }
        return parsedResponseElements;
    }

    private Map<String,ResponseElement> parseDefinitions(JsonNode definitions){
        final Map<String,ResponseElement> definitionMap = new HashMap<String, ResponseElement>();
        Iterator<String> fieldNames = definitions.fieldNames();
        while(fieldNames.hasNext()) {
            final String definition = fieldNames.next();
            final ResponseElement response = parse(definition, definitions.get(definition),true);
            definitionMap.put(definition, response);
        }
        return definitionMap;
    }

    private ResponseElement parse(String nodeName, JsonNode node, boolean required){
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

    private ResponseElement parseObject(String objectName,JsonNode node,boolean required){
        final List<ResponseElement> children = new ArrayList<ResponseElement>();
        final JsonNode properties = node.get("properties");
        final List<String> requiredFields = new ArrayList<String>();
        JsonNode requiredFieldsNode = node.get("required");
        if(requiredFieldsNode != null){
            Iterator<JsonNode> iterator = requiredFieldsNode.iterator();
            while(iterator.hasNext()){
                requiredFields.add(iterator.next().asText());
            }
        }
        final Iterator<String> fieldNames = properties.fieldNames();
        while(fieldNames.hasNext()) {
            final String propertyName = fieldNames.next();
            final boolean isRequiredField = requiredFields.contains(propertyName);
            JsonNode property = properties.get(propertyName);
            children.add(parse(propertyName,property,isRequiredField));
        }
        return new ResponseNode(objectName,"object",children,required);
    }

    private boolean isEnum(JsonNode node){
        return node.get("enum") != null;
    }

    private ResponseElement parseEnum(String nodeName, JsonNode node, boolean required) {
        JsonNode values = node.get("enum");
        List<String> enumValues = new ArrayList<String>();
        Iterator<JsonNode> elements = values.elements();
        while (elements.hasNext()){
            final String element = elements.next().asText();
            enumValues.add(element);
        }
        return new ResponseEnum(nodeName,enumValues);
    }

    private boolean isArray(JsonNode node){
        JsonNode type = node.get("type");
        return type != null && type.asText().equals("array");
    }

    private ResponseElement parseArray(String nodeName, JsonNode node, boolean required) {
        final List<ResponseElement> arrayValues = new ArrayList<ResponseElement>();
        arrayValues.add(parse("element",node.get("items"), true));
        String minItems = "0";
        JsonNode minItemsNode = node.get("minItems");
        if(minItems != null){
            minItems = minItemsNode.asText();
        }
        String maxItems = "n";
        JsonNode maxItemsNode = node.get("maxItems");
        if(maxItemsNode != null){
            maxItems = maxItemsNode.asText();
        }
        return new ResponseArray(nodeName,arrayValues,minItems,maxItems,required);
    }

    private boolean isDefined(JsonNode node){
        JsonNode reference = node.get("$ref");
        if(reference != null && reference.asText().contains("#/definitions/")){
            return true;
        }
        return false;
    }

    private ResponseElement parseDefined(String name, JsonNode node, boolean required){
        String reference = node.get("$ref").asText();
        String propertyType = reference.replace("#/definitions/","");
        return new ResponseReference(name,propertyType, reference, required);
    }

    private ResponseElement parseValue(String valueName,JsonNode node,boolean required){
        JsonNode type = node.get("type");
        if(type != null){
            return new ResponseValue(valueName,type.asText(),required);
        }
        return new ResponseValue(valueName,"unknownType",required);
    }
}
