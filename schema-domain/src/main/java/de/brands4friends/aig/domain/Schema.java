package de.brands4friends.aig.domain;

import java.util.*;

public class Schema {

    private final Map<String,ResponseElement> definedTypes;
    private final List<String> sortedDependencies;

    public Schema(Map<String, ResponseElement> definedTypes, List<String> sortedDependencies) {
        this.definedTypes = definedTypes;
        this.sortedDependencies = sortedDependencies;
    }

    public ResponseElement getRoot(){
        return definedTypes.get("Response");
    }

    public List<ResponseElement> getSortedDependencies(){
        final List<ResponseElement> dependencies = new ArrayList<ResponseElement>();
        for(String dependency : sortedDependencies){
            dependencies.add(definedTypes.get(dependency));
        }
        return dependencies;
    }

    public Map<String, ResponseElement> getTypes() {
        return definedTypes;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private final Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();
        private final Map<String,ResponseElement> definedTypes = new HashMap<String, ResponseElement>();


        public Builder addTypeMap(Map<String,ResponseElement> types){
            definedTypes.putAll(types);
            return this;
        }

        private List<String> computeDependencies(){
            List<String> sortedDependencies = new ArrayList<String>();
            sortedDependencies.add("Response");
            ResponseElement element = definedTypes.get("Response");
            List<ResponseElement> children = new ArrayList<ResponseElement>(element.getChildren());
            Set<ResponseElement> alreadySeenChildren = new HashSet<ResponseElement>();
            alreadySeenChildren.add(element);
            while(!children.isEmpty()){
                ResponseElement child = children.remove(0);
                if(child instanceof ResponseReference){
                    String type = ((ResponseReference) child).getType();
                    if(!sortedDependencies.contains(type)){
                        sortedDependencies.add(type);
                        children.add(definedTypes.get(type));
                    }
                }
                alreadySeenChildren.add(child);
                for(ResponseElement grandChild : child.getChildren()){
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
