package com.ractoc.jsqlplus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LeesInvoer {

    public String inputFile = null;

    public String[] process(String inputFile, String[] params) {
        String[] result = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            result = leesFile(in, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] leesFile(BufferedReader in, String[] params) throws IOException {

        StringBuffer result = new StringBuffer();
        ArrayList<String> queries = new ArrayList<String>();
        String line = null;
        while ((line = in.readLine()) != null) {
            line = replaceParameters(line, params);
            if (line.endsWith(";")) {
                result.append(line.substring(0, line.length() - 1));
                queries.add(result.toString());
                result = new StringBuffer();
            } else {
                result.append(line);
                result.append("\n");
            }
        }
        return queries.toArray(new String[0]);
    }

    private String replaceParameters(String regel, String[] parameters) {
        String result = regel;
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                if (regel.contains("&" + (i + 1))) {
                    System.out.println("Old: " + regel);
                    result = regel.replaceAll("&" + (i + 1), parameters[i]);
                    System.out.println("New: " + result + "\n");
                }
            }
        }
        return result;
    }
}
