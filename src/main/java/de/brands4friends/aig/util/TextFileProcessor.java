package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ApiDoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFileProcessor implements FileProcessor {

    @Override
    public ApiDoc readFromFile(String fileName) throws IOException {
        ApiDoc.Builder builder = ApiDoc.builder();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        while(line != null){
            parseLine(line,builder);
            line = reader.readLine();
        }
        return builder.build();
    }

    private void parseLine(String line, ApiDoc.Builder builder) {
        String[] parts = line.split("##");
        builder.addCall(parts[0].trim(),parts[1].trim(),parts[2].trim());
    }


}
