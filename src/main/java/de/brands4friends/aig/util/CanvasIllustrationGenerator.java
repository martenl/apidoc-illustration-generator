package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseDescription;
import de.brands4friends.aig.domain.ResponseElement;
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
    private ResponseElementExtentProvider extentProvider;
    private DefaultConfiguration<ResponseElement> configuration;


    public CanvasIllustrationGenerator(CanvasProvider canvasProvider) {
        this.canvasProvider = canvasProvider;
    }

    @Override
    public void createIllustration(ResponseDescription responseDescription, String outputFileName, int outerboundX) throws IOException {
        Canvas canvas = canvasProvider.provideCanvas(1000,1000);
        extentProvider = new ResponseElementExtentProvider(25,canvas.getCanvasContent().getFontMetrics());
        double gapBetweenLevels = 20;
        double gapBetweenNodes = 10;
        configuration = new DefaultConfiguration<ResponseElement>(
                gapBetweenLevels, gapBetweenNodes, Configuration.Location.Left, Configuration.AlignmentInLevel.TowardsRoot);
        int offsetX = 20;
        int offsetY = 70;
        for(ResponseElement type : responseDescription.getSortedDependencies()){
            TreeLayout<ResponseElement> treeLayout = createLayout(type);
            offsetY = drawLayout(treeLayout, canvas, offsetX, offsetY,outerboundX);
        }
        canvasProvider.storeCanvas(canvas,outputFileName,offsetY,outerboundX);
    }

     private TreeLayout<ResponseElement> createLayout(ResponseElement element){
        DefaultTreeForTreeLayout<ResponseElement> tree = new DefaultTreeForTreeLayout<ResponseElement>(element);
        for(ResponseElement child : element.getChildren()){
            addElement(child, element, tree);
        }
        return new TreeLayout<ResponseElement>(tree,extentProvider,configuration);
    }

    private int drawLayout(TreeLayout<ResponseElement> treeLayout, Canvas canvas, int offsetX, int offsetY,int outerboundX){
        List<ResponseElement> outOfBounds = getOutOfBoundsElements(treeLayout.getTree().getRoot(),treeLayout,outerboundX-offsetX);
        drawVertices(treeLayout, outOfBounds, canvas, offsetX, offsetY);
        drawEdges(treeLayout, outOfBounds, canvas, offsetX, offsetY);
        offsetY = offsetY + (int)treeLayout.getBounds().getHeight() + 20;
        for(ResponseElement outOfBoundElement : outOfBounds){
            TreeLayout<ResponseElement> outOfBoundsElementLayout = createLayout(outOfBoundElement);
            offsetY = drawLayout(outOfBoundsElementLayout,canvas,offsetX,offsetY,outerboundX);
        }
        return offsetY;
    }

    private void addElement(ResponseElement element,ResponseElement root,DefaultTreeForTreeLayout<ResponseElement> tree){
        tree.addChild(root, element);
        if(element.isNode()){
            for(ResponseElement child : element.getChildren()){
                addElement(child, element, tree);
            }
        }
    }

    private void drawVertices(TreeLayout<ResponseElement> treeLayout,List<ResponseElement> outOfBounds, Canvas canvas, int offsetX, int offsetY) {
        for (ResponseElement element : treeLayout.getNodeBounds().keySet()) {
            if(isAncestorOfAny(element, outOfBounds)){
                continue;
            }
            Rectangle2D.Double box = addOffset(treeLayout.getNodeBounds().get(element),offsetX,offsetY);
            canvas.drawVertex(element,box);
        }
    }

    private void drawEdges(TreeLayout<ResponseElement> treeLayout, List<ResponseElement> outOfBounds, Canvas canvas, int offsetX, int offsetY){
        TreeForTreeLayout<ResponseElement> tree = treeLayout.getTree();
        for(ResponseElement element : treeLayout.getNodeBounds().keySet()){
            if(tree.isLeaf(element) || outOfBounds.contains(element) || isAncestorOfAny(element,outOfBounds)){
                continue;
            }
            canvas.drawEdgesToChildren(treeLayout,element,offsetX,offsetY);
        }
    }

    private Rectangle2D.Double addOffset(Rectangle2D box,int offsetX, int offsetY){
        return new Rectangle2D.Double(box.getX()+offsetX,box.getY()+offsetY,box.getWidth(),box.getHeight());
    }

    private List<ResponseElement> getOutOfBoundsElements(ResponseElement root,TreeLayout<ResponseElement> layout,int outerXBound){
        final List<ResponseElement> outOfBoundsElements = new ArrayList<ResponseElement>();
        Queue<ResponseElement> elementQueue = new LinkedList<ResponseElement>();
        elementQueue.offer(root);
        while (!elementQueue.isEmpty()){
            final ResponseElement element = elementQueue.poll();
            if(allInsideBounds(element.getChildren(),layout,outerXBound)){
                elementQueue.addAll(element.getChildren());
            }else{
                outOfBoundsElements.add(element);
            }
        }
        return outOfBoundsElements;
    }

    private boolean allInsideBounds(List<ResponseElement> elements,TreeLayout<ResponseElement> layout,int outerXBound){
        for(ResponseElement element : elements){
            Rectangle2D.Double bound = layout.getNodeBounds().get(element);
            if(bound.getX()+bound.getWidth() >  outerXBound){
                return false;
            }
        }
        return true;
    }

    private boolean isAncestorOfAny(ResponseElement element,List<ResponseElement> ancestors){
        for(ResponseElement ancestor : ancestors){
            if(ancestor.isAncestor(element)){
                return true;
            }
        }
        return false;
    }
}
