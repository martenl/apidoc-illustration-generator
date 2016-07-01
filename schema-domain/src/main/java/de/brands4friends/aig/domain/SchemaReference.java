package de.brands4friends.aig.domain;

import java.util.ArrayList;
import java.util.List;

public class SchemaReference implements SchemaElement {

    private final String name;
    private final String type;
    private final String reference;
    private final boolean required;

    public SchemaReference(String name, String type, String reference, boolean required) {
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
    public List<SchemaElement> getChildren() {
        return new ArrayList<SchemaElement>();
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
    public boolean isAncestor(SchemaElement element) {
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
        if (!(o instanceof SchemaReference)){
            return false;
        }

        SchemaReference that = (SchemaReference) o;
        return required == that.required
                && name.equals(that.name)
                && reference.equals(that.reference)
                && type.equals(that.type);
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
