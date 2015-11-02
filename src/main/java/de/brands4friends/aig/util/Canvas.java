package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.*;
import org.abego.treelayout.TreeLayout;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Canvas {

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

    private final Graphics2D canvasContent;

    public Canvas(Graphics2D canvasContent) {
        this.canvasContent = canvasContent;
        canvasContent.setFont(DEFAULT_FONT);
        canvasContent.setColor(DEFAULT_COLOR);
    }

    public void drawVertex(ResponseElement element, Rectangle2D.Double box) {
        if(element instanceof ResponseNode){
            drawNodeVertex(element, box);
            return;
        }
        if(element instanceof ResponseValue){
            drawValueVertex(element, box);
            return;
        }
        if(element instanceof ResponseArray){
            drawArrayVertex(element,box);
            return;
        }
        if(element instanceof ResponseEnum){
            drawEnumVertex(element,box);
            return;
        }
        if(element instanceof ResponseReference){
            drawDefinedVertex(element,box);
            return;
        }
    }

    private void drawEnumVertex(ResponseElement element, Rectangle2D.Double box) {
        if(element.isRequired()){
            canvasContent.draw(box);
        }else{
            drawNotRequiredBox(box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(box,caption);
    }

    private void drawNodeVertex(ResponseElement element, Rectangle2D.Double box) {
        drawBoxBackground(box,NODE_COLOR);
        if(element.isRequired()) {
            canvasContent.draw(box);
        }else{
            drawNotRequiredBox(box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(box, caption);
    }

    private void drawDefinedVertex(ResponseElement element, Rectangle2D.Double box) {
        drawBoxBackground(box,DEFINED_COLOR);
        if(!element.isRequired()){
            drawNotRequiredBox(box);
        }else{
            canvasContent.draw(box);
        }
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(box, caption);
    }

    private void drawArrayVertex(ResponseElement element, Rectangle2D.Double box) {
        ResponseArray responseArray = (ResponseArray) element;
        drawBoxBackground(box, ARRAY_COLOR);
        if(!responseArray.isRequired()){
            drawNotRequiredBox(box);
        }else{
            canvasContent.draw(box);
        }
        int fontSize = canvasContent.getFont().getSize();
        final String caption = responseArray.getName()+" : "+responseArray.getType()+"["+responseArray.getMin()+" : "+responseArray.getMax()+"]";
        drawCaption(box, caption);
    }

    private void drawValueVertex(ResponseElement element, Rectangle2D.Double box) {
        final String caption = element.getName()+" : "+element.getType();
        drawCaption(box, caption);
        if(element.isRequired()){
            canvasContent.draw(box);
        }else{
            drawNotRequiredBox(box);
        }
    }

    public void drawEdgesToChildren(TreeLayout<ResponseElement> treeLayout, ResponseElement element,int offsetX,int offsetY){
        canvasContent.setColor(EDGE_COLOR);
        Rectangle2D.Double parentBounds = addOffset(treeLayout.getNodeBounds().get(element), offsetX, offsetY);
        int parentX = (int) (parentBounds.getX()+parentBounds.getWidth());
        int parentY = (int) (parentBounds.getY() + parentBounds.getHeight()/2);
        java.util.List<ResponseElement> children = element.getChildren();
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
            canvasContent.drawLine(x, y, middleX, y);
        }
        canvasContent.drawLine(middleX, maxY, middleX, minY);
        canvasContent.drawLine(parentX, parentY, middleX, parentY);
        canvasContent.setColor(DEFAULT_COLOR);
    }

    private Rectangle2D.Double addOffset(Rectangle2D box,int offsetX, int offsetY){
        return new Rectangle2D.Double(box.getX()+offsetX,box.getY()+offsetY,box.getWidth(),box.getHeight());
    }

    private void drawNotRequiredBox(Rectangle2D.Double box) {
        Stroke stroke = canvasContent.getStroke();
        canvasContent.setStroke(DASHED_STROKE);
        canvasContent.draw(box);
        canvasContent.setStroke(stroke);
    }

    private void drawBoxBackground(Rectangle2D.Double box,Color color){
        Color defaultColor = canvasContent.getColor();
        canvasContent.setColor(color);
        canvasContent.fill(box);
        canvasContent.setColor(defaultColor);
    }

    private void drawCaption(Rectangle2D.Double box, String caption){
        final int fontSize = canvasContent.getFont().getSize();
        final int x = ((int) box.x + fontSize / 2 + 2);
        final int y = ((int) box.y + fontSize + 1);
        Color defaultColor = canvasContent.getColor();
        canvasContent.setColor(CAPTION_COLOR);
        canvasContent.drawString(caption, x, y);
        canvasContent.setColor(defaultColor);
    }

    public Graphics2D getCanvasContent() {
        return canvasContent;
    }


}
