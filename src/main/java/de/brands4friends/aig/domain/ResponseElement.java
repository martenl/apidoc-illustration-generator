package de.brands4friends.aig.domain;

import java.util.List;

public interface ResponseElement {

    boolean isNode();
    List<ResponseElement> getChildren();
    String getName();
    String getType();
    boolean isRequired();
    boolean isAncestor(ResponseElement element);

}
