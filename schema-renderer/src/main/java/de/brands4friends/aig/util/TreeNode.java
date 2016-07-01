package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.SchemaElement;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private final SchemaElement element;
    private final List<TreeNode> children;

    public TreeNode(SchemaElement element) {
        this.element = element;
        children = new ArrayList<TreeNode>();
        for(SchemaElement child : element.getChildren()){
            children.add(new TreeNode(child));
        }
    }

    public SchemaElement getElement() {
        return element;
    }

    public List<TreeNode> getChildren(){
        return children;
    }
}
