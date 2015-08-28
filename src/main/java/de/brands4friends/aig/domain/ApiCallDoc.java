package de.brands4friends.aig.domain;

public class ApiCallDoc {

    private final ApiCall apiCall;
    private final ApiCallResponse response;

    public ApiCallDoc(ApiCall apiCall, ApiCallResponse response) {
        this.apiCall = apiCall;
        this.response = response;
    }

    public ApiCall getApiCall() {
        return apiCall;
    }

    public ApiCallResponse getResponse() {
        return response;
    }
}
