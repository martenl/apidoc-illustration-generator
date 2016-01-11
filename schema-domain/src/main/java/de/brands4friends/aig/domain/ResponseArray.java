package de.brands4friends.aig.domain;

import java.util.List;

public class ResponseArray implements ResponseElement {

    private final String name;
    private final List<ResponseElement> children;
    private final String min;
    private final String max;
    private final boolean required;

    public ResponseArray(String name, List<ResponseElement> children, String min, String max, boolean required) {
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
    public List<ResponseElement> getChildren() {
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
    public boolean isAncestor(ResponseElement element) {
        if(children.contains(element)){
            return true;
        }
        for(ResponseElement child : children){
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
        if (!(o instanceof ResponseArray)){
            return false;
        }

        ResponseArray that = (ResponseArray) o;

        if (required != that.required){
            return false;
        }
        if (!children.equals(that.children)){
            return false;
        }
        if (!max.equals(that.max)){
            return false;
        }
        if (!min.equals(that.min)){
            return false;
        }
        if (!name.equals(that.name)){
            return false;
        }

        return true;
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
