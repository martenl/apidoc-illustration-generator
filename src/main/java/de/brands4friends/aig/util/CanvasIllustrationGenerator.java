package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.*;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CanvasIllustrationGenerator implements IllustrationGenerator {

    public static final Color DEFAULT_COLOR = new Color(0x0d0d0d);
    public static final Color EDGE_COLOR = new Color(0x4C4C4C);
    public static final Color CAPTION_COLOR = new Color(0x131313);
    public static final Color NODE_COLOR = new Color(0xDBEAE4);
    public static final Color DEFINED_COLOR = new Color(0xF6E9C6);
    public static final Color ARRAY_COLOR = new Color(0xE3FAC6);

    final float dash1[] = {5.0f};
    final BasicStroke DASHED_STROKE =
            new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);
    private final Font DEFAULT_FONT = new Font("Arial",Font.PLAIN,14);
    private final CanvasProvider canvasProvider;
    private ResponseElementExtentProvider extentProvider;
    private DefaultConfiguration<ResponseElement> configuration;

    public CanvasIllustrationGenerator(CanvasProvider canvasProvider) {
        this.canvasProvider = canvasProvider;
    }

    @Override
    public void createIllustration(ResponseDescription responseDescription, String outputFileName, int outerboundX) throws IOException {
        Graphics2D canvas = canvasProvider.provideCanvas(1000,1000);
        canvas.setFont(DEFAULT_FONT);
        canvas.setColor(DEFAULT_COLOR);
        extentProvider = new ResponseElementExtentProvider(25,canvas.getFontMetrics());
        double gapBetweenLevels = 20;
        double gapBetweenNodes = 10;
        configuration = new DefaultConfiguration<ResponseElement>(
                gapBetweenLevels, gapBetweenNodes, Configuration.Location.Left, Configuration.AlignmentInLevel.TowardsRoot);
        int offsetX = 20;
        int offsetY = 70;
        for(String elementName : responseDescription.getTypes().keySet()){
            TreeLayout<ResponseElement> treeLayout = createLayout(responseDescription.getTypes().get(elementName));
            offsetY = drawLayout(treeLayout, canvas, offsetX, offsetY,outerboundX);
        }
        canvasProvider.storeCanvas(canvas,outputFileName,offsetY,outerboundX);
    }

    private TreeLayout<ResponseElement> createLayout(ResponseElement element){
        DefaultTreeForTreeLayout<ResponseElement> tree = new DefaultTreeForTreeLayout<ResponseElement>(element);
        for(ResponseElement child : element.getChildren()){
            addElement(child,element,tree);
        }
        return new TreeLayout<ResponseElement>(tree,extentProvider,configuration);
    }

    private int drawLayout(TreeLayout<ResponseElement> treeLayout, Graphics2D canvas, int offsetX, int offsetY,int outerboundX){
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

    private void drawVertices(TreeLayout<ResponseElement> treeLayout,List<ResponseElement> outOfBounds, Graphics2D canvas, int offsetX, int offsetY) {
        for (ResponseElement element : treeLayout.getNodeBounds().keySet()) {
            if(isAncestorOfAny(element, outOfBounds)){
                continue;
            }
            Rectangle2D.Double box = addOffset(treeLayout.getNodeBounds().get(element),offsetX,offsetY);
            drawVertex(element, canvas, box);
        }
    }

    private void drawVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        if(element instanceof ResponseNode){
            drawNodeVertex(element, canvas, box);
            return;
        }
        if(element instanceof ResponseValue){
            drawValueVertex(element, canvas, box);
            return;
        }
        if(element instanceof ResponseArray){
            drawArrayVertex(element,canvas,box);
            return;
        }
        if(element instanceof ResponseEnum){
            drawEnumVertex(element,canvas,box);
        }
        if(element instanceof ResponseReference){
            drawDefinedVertex(element, canvas, box);
        }
    }

    private void drawEnumVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        if(element.isRequired()){
            canvas.draw(box);
        }else{
            drawNotRequiredBox(canvas,box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(canvas,box,caption);
    }

    private void drawNodeVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        drawBoxBackground(canvas,box,NODE_COLOR);
        if(element.isRequired()) {
            canvas.draw(box);
        }else{
            drawNotRequiredBox(canvas,box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(canvas, box, caption);
    }

    private void drawDefinedVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        drawBoxBackground(canvas,box,DEFINED_COLOR);
        if(!element.isRequired()){
            drawNotRequiredBox(canvas,box);
        }else{
            canvas.draw(box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(canvas, box, caption);
    }

    private void drawArrayVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        ResponseArray responseArray = (ResponseArray) element;
        drawBoxBackground(canvas,box,ARRAY_COLOR);
        if(!responseArray.isRequired()){
            drawNotRequiredBox(canvas,box);
        }else{
            canvas.draw(box);
        }
        int fontSize = canvas.getFont().getSize();
        final String caption = responseArray.getName()+" : "+responseArray.getType()+"["+responseArray.getMin()+" : "+responseArray.getMax()+"]";
        drawCaption(canvas, box, caption);
    }

    private void drawValueVertex(ResponseElement element, Graphics2D canvas, Rectangle2D.Double box) {
        final String caption = element.getName()+" : "+element.getType();
        canvas.getFontMetrics().stringWidth(caption);
        drawCaption(canvas, box, caption);
        if(!element.isRequired()){
            drawNotRequiredBox(canvas,box);
        }else{
            canvas.draw(box);
        }
    }

    private void drawEdges(TreeLayout<ResponseElement> treeLayout, List<ResponseElement> outOfBounds, Graphics2D canvas, int offsetX, int offsetY){
        canvas.setColor(EDGE_COLOR);
        TreeForTreeLayout<ResponseElement> tree = treeLayout.getTree();
        for(ResponseElement element : treeLayout.getNodeBounds().keySet()){
            if(tree.isLeaf(element) || outOfBounds.contains(element) || isAncestorOfAny(element,outOfBounds)){
                continue;
            }
            drawEdgesToChildren(treeLayout,canvas,element,offsetX,offsetY);
        }
        canvas.setColor(DEFAULT_COLOR);
    }

    private void drawEdgesToChildren(TreeLayout<ResponseElement> treeLayout, Graphics2D canvas,ResponseElement element,int offsetX,int offsetY){
        Rectangle2D.Double parentBounds = addOffset(treeLayout.getNodeBounds().get(element), offsetX, offsetY);
        int parentX = (int) (parentBounds.getX()+parentBounds.getWidth());
        int parentY = (int) (parentBounds.getY() + parentBounds.getHeight()/2);
        List<ResponseElement> children = element.getChildren();
        ResponseElement firstChild = children.get(0);
        Rectangle2D.Double minBounds = addOffset(treeLayout.getNodeBounds().get(firstChild), offsetX, offsetY);
        final int minY = (int) (minBounds.getY() + minBounds.getHeight()/2);
        int middleX = (int) (minBounds.getX()*.8 + parentX*.2);
        ResponseElement lastChild = children.get(children.size() - 1);
        Rectangle2D.Double maxBounds = addOffset(treeLayout.getNodeBounds().get(lastChild), offsetX, offsetY);
        final int maxY = (int) (maxBounds.getY() + maxBounds.getHeight()/2);
        for(ResponseElement child : children){
            Rectangle2D.Double bounds = addOffset(treeLayout.getNodeBounds().get(child), offsetX, offsetY);
            int x = (int) bounds.getX();
            int y = (int) (bounds.getY() + bounds.getHeight()/2);
            canvas.drawLine(x, y, middleX, y);
        }
        canvas.drawLine(middleX,maxY,middleX,minY);
        canvas.drawLine(parentX,parentY,middleX,parentY);
    }

    private void drawNotRequiredBox(Graphics2D canvas, Rectangle2D.Double box) {
        Stroke stroke = canvas.getStroke();
        canvas.setStroke(DASHED_STROKE);
        canvas.draw(box);
        canvas.setStroke(stroke);
    }

    private void drawBoxBackground(Graphics2D canvas,Rectangle2D.Double box,Color color){
        Color defaultColor = canvas.getColor();
        canvas.setColor(color);
        canvas.fill(box);
        canvas.setColor(defaultColor);
    }

    private void drawCaption(Graphics2D canvas,Rectangle2D.Double box, String caption){
        final int fontSize = canvas.getFont().getSize();
        final int x = ((int) box.x + fontSize / 2 + 2);
        final int y = ((int) box.y + fontSize + 1);
        Color defaultColor = canvas.getColor();
        canvas.setColor(CAPTION_COLOR);
        canvas.drawString(caption,x,y);
        canvas.setColor(defaultColor);
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
