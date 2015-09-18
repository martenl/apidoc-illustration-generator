package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseDescription {

    private final List<ApiDocCategory> categories;
    private final Map<String,ResponseElement> definedTypes;

    public ResponseDescription(List<ApiDocCategory> categories, Map<String, ResponseElement> definedTypes) {
        this.categories = categories;
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

        public Builder addType(final String name,ResponseElement definition){
            definedTypes.put(name,definition);
            return this;
        }

        public Builder addTypeMap(Map<String,ResponseElement> types){
            definedTypes.putAll(types);
            return this;
        }

        public ResponseDescription build(){
            List<ApiDocCategory> categories = buildCategories();
            return new ResponseDescription(categories, definedTypes);
        }

        private List<ApiDocCategory> buildCategories() {
            List<ApiDocCategory> apiDocCategories = new ArrayList<ApiDocCategory>();
            for(String category : data.keySet()){
                List<ApiCallDoc> apiCallDocs = buildApiCallDocs(category);
                apiDocCategories.add(new ApiDocCategory(category, apiCallDocs));
            }
            return apiDocCategories;
        }

        private List<ApiCallDoc> buildApiCallDocs(final String category){
            List<ApiCallDoc> apiCallDocs = new ArrayList<ApiCallDoc>();
            Map<String,String> apiCallsForCategory = data.get(category);
            for(String apiCall : apiCallsForCategory.keySet()){
                final String response = apiCallsForCategory.get(apiCall);
                apiCallDocs.add(new ApiCallDoc(new ApiCall(apiCall),new ApiCallResponse(response,null)));
            }
            return apiCallDocs;
        }
    }

}
