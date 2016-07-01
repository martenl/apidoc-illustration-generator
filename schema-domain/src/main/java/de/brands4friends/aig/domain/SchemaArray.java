package de.brands4friends.aig.domain;

import java.util.List;

public class SchemaArray implements SchemaElement {

    private final String name;
    private final List<SchemaElement> children;
    private final String min;
    private final String max;
    private final boolean required;

    public SchemaArray(String name, List<SchemaElement> children, String min, String max, boolean required) {
        this.name = name;
        this.children = children;
        this.min = min;
        this.max = max;
        this.required = required;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public List<SchemaElement> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "array";
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isAncestor(SchemaElement element) {
        if(children.contains(element)){
            return true;
        }
        for(SchemaElement child : children){
            if(child.isAncestor(element)){
                return true;
            }
        }
        return false;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    @Override
    public String toString(){
        return name+" : array["+min+" : "+max+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof SchemaArray)){
            return false;
        }

        SchemaArray that = (SchemaArray) o;
        return required == that.required
                && children.equals(that.children)
                && max.equals(that.max)
                && min.equals(that.min)
                && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + children.hashCode();
        result = 31 * result + min.hashCode();
        result = 31 * result + max.hashCode();
        result = 31 * result + (required ? 1 : 0);
        return result;
    }
}
