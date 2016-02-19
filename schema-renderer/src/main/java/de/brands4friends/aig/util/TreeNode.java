package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseElement;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private final ResponseElement element;
    private final List<TreeNode> children;

    public TreeNode(ResponseElement element) {
        this.element = element;
        children = new ArrayList<TreeNode>();
        for(ResponseElement child : element.getChildren()){
            children.add(new TreeNode(child));
        }
    }

    public ResponseElement getElement() {
        return element;
    }

    public List<TreeNode> getChildren(){
        return children;
    }
}
