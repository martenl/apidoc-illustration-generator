package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.List;

public class SchemaValue implements SchemaElement {

    private final String name;
    private final String type;
    private final boolean required;

    public SchemaValue(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public List<SchemaElement> getChildren() {
        return new ArrayList<SchemaElement>();
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
    public boolean isAncestor(SchemaElement element) {
        return false;
    }

    @Override
    public String toString() {
        return  name + ':' + type ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemaValue)){
            return false;
        }

        SchemaValue that = (SchemaValue) o;

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
