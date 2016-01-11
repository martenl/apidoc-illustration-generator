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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseValue)){
            return false;
        }

        ResponseValue that = (ResponseValue) o;

        if (required != that.required){
            return false;
        }
        if (!name.equals(that.name)){
            return false;
        }
        if (!type.equals(that.type)){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (required ? 1 : 0);
        return result;
    }
}
