package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.List;

public class ResponseValue implements ResponseElement{

    private final String name;
    private final String type;
    private final boolean required;

    public ResponseValue(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
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

    public String getName() {
        return name;
    }

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
    public String toString() {
        return  name + ':' + type ;
    }
}
