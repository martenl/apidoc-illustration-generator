package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiCallDoc;
import de.brands4friends.aig.domain.ApiDoc;
import de.brands4friends.aig.domain.ApiDocCategory;

public class ConsoleIllustrationGenerator implements IllustrationGenerator {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public void createIllustration(ApiDoc apiDoc, String outputFileName) {
        System.out.println("creating illustration");
        for(ApiDocCategory category : apiDoc.getCategories()){
            printCategory(category);
        }
        System.out.println("-------------------------------------");
        System.out.println("-------------START-------------------");
        System.out.println("-------------------------------------");
        System.out.println(builder.toString());
        System.out.println("-------------------------------------");
        System.out.println("--------------END--------------------");
        System.out.println("-------------------------------------");
    }

    private void printCategory(ApiDocCategory apiDocCategory){
        final String name = apiDocCategory.getName();
        builder.append(name).append("\n");
        for(int i = 0;i<name.length();i++){
            builder.append("-");
        }
        builder.append("\n");
        for(ApiCallDoc apiCallDoc : apiDocCategory.getApiCalls()){
            printApiCall(apiCallDoc);
        }
    }

    private void printApiCall(ApiCallDoc apiCallDoc) {
        builder.append("\t")
                .append(apiCallDoc.getApiCall().getApiCall())
                .append("\n")
                .append("\t\tResponse: ")
                .append(apiCallDoc.getResponse().getResponse())
                .append("\n");
    }
}
