package de.brands4friends.aig.domain;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class ResponseEnum implements ResponseElement {

    private final String name;
    private final List<String> values;

    public ResponseEnum(String name, List<String> values) {
        this.name = name;
        this.values = values;
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
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0;i<values.size()-1;i++){
            builder.append(values.get(i)).append(" | ");
        }
        builder.append(values.get(values.size()-1))
                .append("]");
        return builder.toString();
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public boolean isAncestor(ResponseElement element) {
        return false;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("[")
                .append(Joiner.on("|").join(values).toString())
                .append("]");
        return name+" : "+builder.toString();
    }
}
