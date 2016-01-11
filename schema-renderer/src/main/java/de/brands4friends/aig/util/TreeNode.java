package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseElement;

public class TreeNode {

    private final ResponseElement element;

    public TreeNode(ResponseElement element) {
        this.element = element;
    }

    public ResponseElement getElement() {
        return element;
    }
}
