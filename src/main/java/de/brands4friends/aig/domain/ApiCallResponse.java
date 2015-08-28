package de.brands4friends.aig.domain;

public class ApiCallResponse {

    private final String response;

    public ApiCallResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
