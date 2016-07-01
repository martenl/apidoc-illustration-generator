package de.brands4friends.aig.domain;

import java.util.List;

public interface SchemaElement {

    boolean isNode();
    List<SchemaElement> getChildren();
    String getName();
    String getType();
    boolean isRequired();
    boolean isAncestor(SchemaElement element);

}
