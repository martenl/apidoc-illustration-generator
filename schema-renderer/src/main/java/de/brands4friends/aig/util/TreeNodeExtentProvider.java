package de.brands4friends.aig.util;

import org.abego.treelayout.NodeExtentProvider;

import java.awt.*;

public class TreeNodeExtentProvider implements NodeExtentProvider<TreeNode> {

    private final int height;
    private final FontMetrics fontMetrics;

    public TreeNodeExtentProvider(int height, FontMetrics fontMetrics) {
        this.height = height;
        this.fontMetrics = fontMetrics;
    }

    @Override
    public double getWidth(TreeNode treeNode) {
        return fontMetrics.stringWidth(treeNode.getElement().toString()) +20;
    }

    @Override
    public double getHeight(TreeNode treeNode) {
        return height;
    }
}