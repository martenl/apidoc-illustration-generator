package de.brands4friends.aig.domain;

import java.util.List;

public class ApiDocCategory {

    private final String name;
    private final List<ApiCallDoc> apiCalls;

    public ApiDocCategory(String name, List<ApiCallDoc> apiCalls) {
        this.name = name;
        this.apiCalls = apiCalls;
    }

    public String getName() {
        return name;
    }

    public List<ApiCallDoc> getApiCalls() {
        return apiCalls;
    }
}
