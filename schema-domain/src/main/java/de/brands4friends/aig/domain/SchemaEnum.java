package de.brands4friends.aig.domain;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class SchemaEnum implements SchemaElement {

    private final String name;
    private final List<String> values;

    public SchemaEnum(String name, List<String> values) {
        this.name = name;
        this.values = values;
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
    public boolean isAncestor(SchemaElement element) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof SchemaEnum)){
            return false;
        }

        SchemaEnum that = (SchemaEnum) o;
        return name.equals(that.name) && values.equals(that.values);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }
}
