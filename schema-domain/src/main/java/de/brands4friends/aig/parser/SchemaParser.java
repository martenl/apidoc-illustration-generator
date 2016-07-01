package de.brands4friends.aig.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.brands4friends.aig.domain.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SchemaParser implements FileProcessor {

    private String directory;
    private final JacksonParser parser;

    public SchemaParser(JacksonParser parser) {
        this.parser = parser;
    }

    @Override
    public Schema readFromFile(String fileName) throws IOException {
        directory = fileName.substring(0,fileName.lastIndexOf(File.separator));
        Schema.Builder builder = Schema.builder();
        Map<String,SchemaElement> definitionMap = parser.parseFile(fileName);
        Map<String,SchemaElement> externalDefinitions = loadExternalDefinitions(computeExternalReferences(definitionMap.values()));
        definitionMap.putAll(externalDefinitions);
        builder.addTypeMap(definitionMap);
        return builder.build();
    }

    private List<String> computeExternalReferences(Collection<SchemaElement> schemaElements){
        List<String> externalReferences = new ArrayList<String>();
        for(SchemaElement schemaElement : schemaElements){
            externalReferences.addAll(computeExternalReferences(schemaElement));
        }
        return externalReferences;
    }

    private List<String> computeExternalReferences(SchemaElement schemaElement) {
        List<String> externalReferences = new ArrayList<String>();
        if(schemaElement instanceof SchemaReference){
            SchemaReference responseReference = (SchemaReference) schemaElement;
            String reference = responseReference.getReference();
            if(!reference.startsWith("#")){
                externalReferences.add(reference);
            }
        }
        if(schemaElement instanceof SchemaNode || schemaElement instanceof SchemaArray){
            externalReferences.addAll(computeExternalReferences(schemaElement.getChildren()));
        }
        return externalReferences;
    }

    private List<String> computeInternalReferences(Collection<SchemaElement> schemaElements){
        List<String> internalReferences = new ArrayList<String>();
        for(SchemaElement schemaElement : schemaElements){
            internalReferences.addAll(computeInternalReferences(schemaElement));
        }
        return internalReferences;
    }

    private List<String> computeInternalReferences(SchemaElement schemaElement) {
        List<String> internalReferences = new ArrayList<String>();
        if(schemaElement instanceof SchemaReference){
            SchemaReference responseReference = (SchemaReference) schemaElement;
            String reference = responseReference.getReference();
            if(reference.startsWith("#")){
                internalReferences.add(reference);
            }
        }
        if(schemaElement instanceof SchemaNode || schemaElement instanceof SchemaArray){
            internalReferences.addAll(computeInternalReferences(schemaElement.getChildren()));
        }
        return internalReferences;
    }

    private Map<String, SchemaElement> loadExternalDefinitions(List<String> externalDefinitions) throws IOException {
        Map<String, SchemaElement> definitionMap = new HashMap<String, SchemaElement>();
        Map<String,List<String>> fileNameToTypes = computeFileNameToTypesMap(externalDefinitions);
        for(String fileName : fileNameToTypes.keySet()){
            Map<String,SchemaElement> types = parser.parseFile(fileName);
            for(String typeName : fileNameToTypes.get(fileName)){
                definitionMap.put(typeName, types.get(typeName));
                List<String> internalReferences = computeInternalReferences(types.get(typeName));
                for(String internalReference : internalReferences){
                    final String referenceName = internalReference.replace("#/definitions/","");
                    definitionMap.put(referenceName,types.get(referenceName));
                }
            }
        }
        return definitionMap;
    }

    private Map<String, List<String>> computeFileNameToTypesMap(List<String> externalDefinitions) {
        Map<String,List<String>> fileNameToTypes = new HashMap<String, List<String>>();
        for(String externalDefinition : externalDefinitions){
            final int fileStartIndex = externalDefinition.indexOf("#");
            String file = directory+externalDefinition.substring(0,fileStartIndex);
            final int typeStartIndex = externalDefinition.lastIndexOf("/")+1;
            String type = externalDefinition.substring(typeStartIndex);
            if(fileNameToTypes.containsKey(file)){
                fileNameToTypes.get(file).add(type);
            }else{
                List<String> types = new ArrayList<String>();
                types.add(type);
                fileNameToTypes.put(file,types);
            }
        }
        return fileNameToTypes;
    }

    public static FileProcessor yamlParser(){
        return new SchemaParser(new JacksonParser(new ObjectMapper(new YAMLFactory())));
    }

    public static FileProcessor jsonParser(){
        return new SchemaParser(new JacksonParser(new ObjectMapper()));
    }
}
