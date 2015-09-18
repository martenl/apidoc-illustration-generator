package de.brands4friends.aig.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.brands4friends.aig.domain.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonProcessor implements FileProcessor {

    private String directory;
    private final JacksonParser parser;

    public JsonProcessor() {
        final ObjectMapper mapper = new ObjectMapper();
        parser = new JacksonParser(mapper);
    }

    @Override
    public ResponseDescription readFromFile(String fileName) throws IOException {
        directory = fileName.substring(0,fileName.lastIndexOf(File.separator));
        ResponseDescription.Builder builder = ResponseDescription.builder();
        Map<String,ResponseElement> definitionMap = parser.parseFile(fileName);
        Map<String,ResponseElement> externalDefinitions = loadExternalDefinitions(computeExternalReferences(definitionMap.values()));
        definitionMap.putAll(externalDefinitions);
        builder.addTypeMap(definitionMap);
        return builder.build();
    }

    private List<String> computeExternalReferences(Collection<ResponseElement> responseElements){
        List<String> externalReferences = new ArrayList<String>();
        for(ResponseElement responseElement : responseElements){
            externalReferences.addAll(computeExternalReferences(responseElement));
        }
        return externalReferences;
    }

    private List<String> computeExternalReferences(ResponseElement responseElement) {
        List<String> externalReferences = new ArrayList<String>();
        if(responseElement instanceof ResponseReference){
            ResponseReference responseReference = (ResponseReference) responseElement;
            String reference = responseReference.getReference();
            if(!reference.startsWith("#")){
                externalReferences.add(reference);
            }
        }
        if(responseElement instanceof ResponseNode || responseElement instanceof ResponseArray){
            externalReferences.addAll(computeExternalReferences(responseElement.getChildren()));
        }
        return externalReferences;
    }

    private List<String> computeInternalReferences(Collection<ResponseElement> responseElements){
        List<String> internalReferences = new ArrayList<String>();
        for(ResponseElement responseElement : responseElements){
            internalReferences.addAll(computeInternalReferences(responseElement));
        }
        return internalReferences;
    }

    private List<String> computeInternalReferences(ResponseElement responseElement) {
        List<String> internalReferences = new ArrayList<String>();
        if(responseElement instanceof ResponseReference){
            ResponseReference responseReference = (ResponseReference) responseElement;
            String reference = responseReference.getReference();
            if(reference.startsWith("#")){
                internalReferences.add(reference);
            }
        }
        if(responseElement instanceof ResponseNode || responseElement instanceof ResponseArray){
            internalReferences.addAll(computeInternalReferences(responseElement.getChildren()));
        }
        return internalReferences;
    }

    private Map<String, ResponseElement> loadExternalDefinitions(List<String> externalDefinitions) throws IOException {
        Map<String, ResponseElement> definitionMap = new HashMap<String, ResponseElement>();
        Map<String,List<String>> fileNameToTypes = computeFileNameToTypesMap(externalDefinitions);
        for(String fileName : fileNameToTypes.keySet()){
            Map<String,ResponseElement> types = parser.parseFile(fileName);
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
                fileNameToTypes.put(file,Arrays.asList(type));
            }
        }
        return fileNameToTypes;
    }
}
