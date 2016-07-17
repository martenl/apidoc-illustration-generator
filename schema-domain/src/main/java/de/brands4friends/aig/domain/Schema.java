package de.brands4friends.aig.domain;

import java.util.*;

public class Schema {

    private final Map<String,SchemaElement> definedTypes;
    private final List<String> sortedDependencies;

    public Schema(Map<String, SchemaElement> definedTypes, List<String> sortedDependencies) {
        this.definedTypes = definedTypes;
        this.sortedDependencies = sortedDependencies;
    }

    public SchemaElement getRoot(){
        return definedTypes.get("Response");
    }

    public List<SchemaElement> getSortedDependencies(){
        final List<SchemaElement> dependencies = new ArrayList<SchemaElement>();
        for(String dependency : sortedDependencies){
            dependencies.add(definedTypes.get(dependency));
        }
        return dependencies;
    }

    public Map<String, SchemaElement> getTypes() {
        return definedTypes;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private final Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();
        private final Map<String,SchemaElement> definedTypes = new HashMap<String, SchemaElement>();


        public Builder addTypeMap(Map<String,SchemaElement> types){
            definedTypes.putAll(types);
            return this;
        }

        private List<String> computeDependencies(){
            List<String> sortedDependencies = new ArrayList<String>();
            if(definedTypes.isEmpty()){
                return sortedDependencies;
            }
            sortedDependencies.add("Response");
            SchemaElement element = definedTypes.get("Response");
            List<SchemaElement> children = new ArrayList<SchemaElement>(element.getChildren());
            Set<SchemaElement> alreadySeenChildren = new HashSet<SchemaElement>();
            alreadySeenChildren.add(element);
            while(!children.isEmpty()){
                SchemaElement child = children.remove(0);
                if(child instanceof SchemaReference){
                    String type = child.getType();
                    if(!sortedDependencies.contains(type)){
                        sortedDependencies.add(type);
                        children.add(definedTypes.get(type));
                    }
                }
                alreadySeenChildren.add(child);
                for(SchemaElement grandChild : child.getChildren()){
                    if(!alreadySeenChildren.contains(grandChild)){
                        children.add(grandChild);
                    }
                }
            }
            return sortedDependencies;
        }

        public Schema build(){
            return new Schema(definedTypes,computeDependencies());
        }
    }

}
