package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.List;

public class ResponseReference implements ResponseElement {

    private final String name;
    private final String type;
    private final String reference;
    private final boolean required;

    public ResponseReference(String name, String type, String reference, boolean required) {
        this.name = name;
        this.type = type;
        this.reference = reference;
        this.required = required;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public List<ResponseElement> getChildren() {
        return new ArrayList<ResponseElement>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isAncestor(ResponseElement element) {
        return false;
    }

    @Override
    public String toString(){
        return name+" : "+type;
    }

    public String getReference(){
        return reference;
    }
}
