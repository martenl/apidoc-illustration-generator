package de.brands4friends.aig.domain;

import java.util.List;

public class ResponseNode implements ResponseElement{

    private final String name;
    private final String type;
    private final List<ResponseElement> children;
    private final boolean required;

    public ResponseNode(String name, String type, List<ResponseElement> children, boolean required) {
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
    public List<ResponseElement> getChildren() {
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


    @Override
    public String toString() {
        return name+" : "+type;
    }
}
