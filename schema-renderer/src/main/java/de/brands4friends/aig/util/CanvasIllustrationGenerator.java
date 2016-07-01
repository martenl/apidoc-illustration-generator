package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.Schema;
import de.brands4friends.aig.domain.SchemaElement;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CanvasIllustrationGenerator implements IllustrationGenerator {

    private final CanvasProvider canvasProvider;
    private TreeNodeExtentProvider extentProvider;
    private DefaultConfiguration<TreeNode> configuration;


    public CanvasIllustrationGenerator(CanvasProvider canvasProvider) {
        this.canvasProvider = canvasProvider;
    }

    @Override
    public void createIllustration(Schema schema, String outputFileName, int outerboundX) throws IOException {
        Canvas canvas = canvasProvider.provideCanvas(1000,1000);
        extentProvider = new TreeNodeExtentProvider(25,canvas.getCanvasContent().getFontMetrics());
        double gapBetweenLevels = 20;
        double gapBetweenNodes = 10;
        configuration = new DefaultConfiguration<TreeNode>(
                gapBetweenLevels,
                gapBetweenNodes,
                Configuration.Location.Left,
                Configuration.AlignmentInLevel.TowardsRoot);
        int offsetX = 20;
        int offsetY = 70;
        for(SchemaElement type : schema.getSortedDependencies()){
            TreeLayout<TreeNode> treeLayout = createLayout(type);
            offsetY = drawLayout(treeLayout, canvas, offsetX, offsetY,outerboundX);
        }
        canvasProvider.storeCanvas(canvas,outputFileName,offsetY,outerboundX);
    }

     private TreeLayout<TreeNode> createLayout(SchemaElement element){
         TreeNode root = new TreeNode(element);
         DefaultTreeForTreeLayout<TreeNode> tree = new DefaultTreeForTreeLayout<TreeNode>(root);
         for(TreeNode child : root.getChildren()){
             addElement(child,root,tree);
         }
         return new TreeLayout<TreeNode>(tree,extentProvider,configuration);
    }

    private void addElement(TreeNode element,TreeNode root,DefaultTreeForTreeLayout<TreeNode> tree){
        tree.addChild(root, element);
        if(element.getElement().isNode()){
            for(TreeNode child : element.getChildren()){
                addElement(child, element, tree);
            }
        }
    }

    private int drawLayout(TreeLayout<TreeNode> treeLayout, Canvas canvas, int offsetX, int offsetY,int outerboundX){
        List<TreeNode> outOfBounds = getOutOfBoundsElements(
                treeLayout.getTree().getRoot(),
                treeLayout,
                outerboundX - offsetX);
        drawVertices(treeLayout, outOfBounds, canvas, offsetX, offsetY);
        drawEdges(treeLayout, outOfBounds, canvas, offsetX, offsetY);
        offsetY = offsetY + (int)treeLayout.getBounds().getHeight() + 20;
        for(TreeNode outOfBoundElement : outOfBounds){
            TreeLayout<TreeNode> outOfBoundsElementLayout = createLayout(outOfBoundElement.getElement());
            offsetY = drawLayout(outOfBoundsElementLayout,canvas,offsetX,offsetY,outerboundX);
        }
        return offsetY;
    }

    private List<TreeNode> getOutOfBoundsElements(
            TreeNode root,
            TreeLayout<TreeNode> layout,
            int outerXBound){
        final List<TreeNode> outOfBoundsElements = new ArrayList<TreeNode>();
        Queue<TreeNode> elementQueue = new LinkedList<TreeNode>();
        elementQueue.offer(root);
        //layout.getTree().
        while (!elementQueue.isEmpty()){
            final TreeNode element = elementQueue.poll();
            if(allInsideBounds(element.getChildren(),layout,outerXBound)){
                elementQueue.addAll(element.getChildren());
            }else{
                outOfBoundsElements.add(element);
            }
        }
        return outOfBoundsElements;
    }

    private boolean allInsideBounds(List<TreeNode> elements,TreeLayout<TreeNode> layout,int outerXBound){
        for(TreeNode element : elements){
            Rectangle2D.Double bound = layout.getNodeBounds().get(element);
            if(bound.getX()+ bound.getWidth() >  outerXBound){
                return false;
            }
        }
        return true;
    }

    private void drawVertices(
            TreeLayout<TreeNode> treeLayout,
            List<TreeNode> outOfBounds,
            Canvas canvas,
            int offsetX,
            int offsetY) {
        for (TreeNode element : treeLayout.getNodeBounds().keySet()) {
            if(isAncestorOfAny(element, outOfBounds)){
                continue;
            }
            Rectangle2D.Double box = addOffset(treeLayout.getNodeBounds().get(element),offsetX,offsetY);
            canvas.drawVertex(element.getElement(), box);
        }
    }

    private void drawEdges(
            TreeLayout<TreeNode> treeLayout,
            List<TreeNode> outOfBounds,
            Canvas canvas,
            int offsetX,
            int offsetY){
        TreeForTreeLayout<TreeNode> tree = treeLayout.getTree();
        for(TreeNode element : treeLayout.getNodeBounds().keySet()){
            if(tree.isLeaf(element) || outOfBounds.contains(element) || isAncestorOfAny(element,outOfBounds)){
                continue;
            }
            canvas.drawEdgesToChildren(treeLayout,element,offsetX,offsetY);
        }
    }

    private boolean isAncestorOfAny(TreeNode element,List<TreeNode> ancestors){
        SchemaElement elementPayload = element.getElement();
        for(TreeNode ancestor : ancestors){
            if(ancestor.getElement().isAncestor(elementPayload)){
                return true;
            }
        }
        return false;
    }

    private Rectangle2D.Double addOffset(Rectangle2D box,int offsetX, int offsetY){
        return new Rectangle2D.Double(box.getX()+offsetX,box.getY()+offsetY,box.getWidth(),box.getHeight());
    }
}
