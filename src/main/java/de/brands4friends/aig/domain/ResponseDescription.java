package de.brands4friends.aig.domain;

import java.util.HashMap;
import java.util.Map;

public class ResponseDescription {

    private final Map<String,ResponseElement> definedTypes;

    public ResponseDescription(Map<String, ResponseElement> definedTypes) {
        this.definedTypes = definedTypes;
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

        public Builder addCall(final String category,final String call, final String response){
            if(!data.containsKey(category)){
                data.put(category,new HashMap<String, String>());
            }
            data.get(category).put(call,response);
            return this;
        }


        public Builder addTypeMap(Map<String,ResponseElement> types){
            definedTypes.putAll(types);
            return this;
        }

        public ResponseDescription build(){
            return new ResponseDescription(definedTypes);
        }
    }

}
