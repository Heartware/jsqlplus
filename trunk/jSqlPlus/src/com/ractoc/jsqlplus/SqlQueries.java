package com.ractoc.jsqlplus;

import java.io.IOException;
import java.sql.SQLException;

public class SqlQueries {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if (args.length < 2) {
            throw new RuntimeException("Expected a minimum of 2 parameters, "
                    + "Database connect string and Script file.");
        }

        String connectString = args[0];
        String scriptFile = args[1];
        String[] params = new String[args.length - 2];

        for (int i = 2; i < args.length; i++) {
            params[i - 2] = args[i];
        }

        SqlQueries sq = new SqlQueries();

        sq.process(connectString, scriptFile, params);
    }

    private void process(String connectString, String scriptFile, String[] params) throws IOException, ClassNotFoundException {
        LeesInvoer li = new LeesInvoer();
        String[] queries = li.process(scriptFile, params);
        ExecuteQuery eq = new ExecuteQuery();
        try {
            for (String query : queries) {
                String[][] result = eq.execute(connectString, query);
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        if (j > 0) {
                            System.out.print(",");
                        }
                        System.out.print(result[i][j]);
                    }
                    System.out.print("\n");
                }
                System.out.println("Number of rows returned: " + result.length);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
