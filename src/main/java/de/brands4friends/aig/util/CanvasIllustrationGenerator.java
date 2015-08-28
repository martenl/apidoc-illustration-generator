package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiCallDoc;
import de.brands4friends.aig.domain.ApiDoc;
import de.brands4friends.aig.domain.ApiDocCategory;

import java.awt.*;
import java.io.IOException;

public class CanvasIllustrationGenerator implements IllustrationGenerator {

    private final CanvasProvider canvasProvider;

    public CanvasIllustrationGenerator(CanvasProvider canvasProvider) {
        this.canvasProvider = canvasProvider;
    }

    @Override
    public void createIllustration(ApiDoc apiDoc, String outputFileName) throws IOException {
        Graphics2D canvas = canvasProvider.provideCanvas(1000,1000);
        int position = 10;
        int offset = 15;
        for(ApiDocCategory category : apiDoc.getCategories()){
            canvas.drawString(category.getName(),10,position);
            position+=offset;
            for(ApiCallDoc apiCallDoc : category.getApiCalls()){
                canvas.drawString(apiCallDoc.getApiCall().getApiCall(),18,position);
                canvas.drawString(apiCallDoc.getResponse().getResponse(),158,position);
                position+=offset;
            }
        }
        canvasProvider.storeCanvas(canvas,outputFileName);
    }
}
