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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof ResponseReference)){
            return false;
        }

        ResponseReference that = (ResponseReference) o;

        if (required != that.required){
            return false;
        }
        if (!name.equals(that.name)){
            return false;
        }
        if (!reference.equals(that.reference)){
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
        result = 31 * result + reference.hashCode();
        result = 31 * result + (required ? 1 : 0);
        return result;
    }
}
