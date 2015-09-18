package de.brands4friends.aig.util;

import de.brands4friends.aig.domain.ResponseDescription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFileProcessor implements FileProcessor {

    @Override
    public ResponseDescription readFromFile(String fileName) throws IOException {
        ResponseDescription.Builder builder = ResponseDescription.builder();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        while(line != null){
            parseLine(line,builder);
            line = reader.readLine();
        }
        return builder.build();
    }

    private void parseLine(String line, ResponseDescription.Builder builder) {
        String[] parts = line.split("##");
        builder.addCall(parts[0].trim(),parts[1].trim(),parts[2].trim());
    }


}
