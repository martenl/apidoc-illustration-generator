package de.brands4friends.aig.domain;

import java.util.List;

public class SchemaNode implements SchemaElement {

    private final String name;
    private final String type;
    private final List<SchemaElement> children;
    private final boolean required;

    public SchemaNode(String name, String type, List<SchemaElement> children, boolean required) {
        this.name = name;
        this.type = type;
        this.children = children;
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
        return type;
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


    @Override
    public String toString() {
        return name+" : "+type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof SchemaNode)){
            return false;
        }

        SchemaNode that = (SchemaNode) o;

        return required == that.required
                &&  children.equals(that.children)
                && name.equals(that.name)
                && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + children.hashCode();
        result = 31 * result + (required ? 1 : 0);
        return result;
    }
}
