package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiDoc;
import de.brands4friends.aig.domain.ResponseElement;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CanvasIllustrationGenerator implements IllustrationGenerator {

    private final CanvasProvider canvasProvider;

    public CanvasIllustrationGenerator(CanvasProvider canvasProvider) {
        this.canvasProvider = canvasProvider;
    }

    @Override
    public void createIllustration(ApiDoc apiDoc, String outputFileName) throws IOException {
        Graphics2D canvas = canvasProvider.provideCanvas(1000,1000);
        TreeLayout<TextInBox> treeLayout = createLayout(apiDoc.getTypes());
        drawVertices(treeLayout,canvas);
        drawEdges(treeLayout,canvas);
        canvasProvider.storeCanvas(canvas,outputFileName);
    }

   private TreeLayout<TextInBox> createLayout(Map<String,ResponseElement> types){
        TextInBox root = new TextInBox("Definitions",70,20);
        DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(root);
        for(ResponseElement element : types.values()){
            addElement(element,root,tree);
        }
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();
       ResponseElementExtentProvider extentProvider = new ResponseElementExtentProvider(20,8);
        DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
                gapBetweenLevels, gapBetweenNodes, Configuration.Location.Left, Configuration.AlignmentInLevel.TowardsRoot);
        TreeLayout<TextInBox> layout = new TreeLayout<TextInBox>(tree,nodeExtentProvider,configuration);
        return layout;
    }

    private void addElement(ResponseElement element,TextInBox root,DefaultTreeForTreeLayout<TextInBox> tree){
        final String caption = element.getName()+":"+element.getType();
        final int width = caption.length()*8;
        TextInBox elementBox = new TextInBox(caption,width,20);
        tree.addChild(root, elementBox);
        if(element.isNode()){
            for(ResponseElement child : element.getChildren()){
                addElement(child, elementBox, tree);
            }
        }
    }

    private void drawVertices(TreeLayout<TextInBox> treeLayout, Graphics2D canvas) {
        for (TextInBox textInBox : treeLayout.getNodeBounds().keySet()) {
            drawVertex(textInBox, canvas, treeLayout.getNodeBounds().get(textInBox));
        }
    }

    private void drawVertex(TextInBox textInBox, Graphics2D canvas, Rectangle2D.Double box) {
        canvas.draw(box);
        int fontSize = canvas.getFont().getSize();
        final int x = (int) box.x + fontSize / 2 + 2;
        final int y = (int) box.y + fontSize + 1;
        canvas.drawString(textInBox.text,x,y);
    }

    private void drawEdges(TreeLayout<TextInBox> treeLayout, Graphics2D canvas){
        TreeForTreeLayout<TextInBox> tree = treeLayout.getTree();
        for(TextInBox tib : treeLayout.getNodeBounds().keySet()){
            if(tree.isLeaf(tib)){
                continue;
            }
            Rectangle2D.Double parentBounds = treeLayout.getNodeBounds().get(tib);
            Iterable<TextInBox> children = tree.getChildren(tib);
            int parentX = (int) (parentBounds.getX()+parentBounds.getWidth());
            int parentY = (int) (parentBounds.getY() + parentBounds.getHeight()/2);
            Iterator<TextInBox> childrenIterator = children.iterator();
            TextInBox child = childrenIterator.next();
            Rectangle2D.Double minBounds = treeLayout.getNodeBounds().get(child);
            final int minY = (int) (minBounds.getY() + minBounds.getHeight()/2);
            int middleX = (int) (minBounds.getX()*.9 + parentX*.1);
            while(childrenIterator.hasNext()){
                Rectangle2D.Double bounds = treeLayout.getNodeBounds().get(child);
                int x = (int) bounds.getX();
                int y = (int) (bounds.getY() + bounds.getHeight()/2);
                canvas.drawLine(x, y, middleX, y);
                child = childrenIterator.next();
            }
            Rectangle2D.Double maxBounds = treeLayout.getNodeBounds().get(child);
            final int maxY = (int) (maxBounds.getY() + maxBounds.getHeight()/2);
            int maxX = (int) maxBounds.getX();
            canvas.drawLine(maxX,maxY,middleX,maxY);
            canvas.drawLine(middleX,maxY,middleX,minY);
            canvas.drawLine(parentX,parentY,middleX,parentY);
        }
    }


}
