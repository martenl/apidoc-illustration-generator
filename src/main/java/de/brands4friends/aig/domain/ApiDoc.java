package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDoc {

    private final List<ApiDocCategory> categories;

    public ApiDoc(List<ApiDocCategory> categories) {
        this.categories = categories;
    }

    public List<ApiDocCategory> getCategories() {
        return categories;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private final Map<String,Map<String,String>> data = new HashMap<String, Map<String, String>>();

        public Builder addCall(final String category,final String call, final String response){
            if(!data.containsKey(category)){
                data.put(category,new HashMap<String, String>());
            }
            data.get(category).put(call,response);
            return this;
        }

        public ApiDoc build(){
            List<ApiDocCategory> categories = buildCategories();
            return new ApiDoc(categories);
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
                apiCallDocs.add(new ApiCallDoc(new ApiCall(apiCall),new ApiCallResponse(response)));
            }
            return apiCallDocs;
        }
    }
}
