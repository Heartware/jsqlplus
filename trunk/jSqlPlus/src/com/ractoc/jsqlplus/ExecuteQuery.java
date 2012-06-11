package com.ractoc.jsqlplus;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ractoc
 */
public class ExecuteQuery {

    private Properties dbAliasses = new Properties();

    public ExecuteQuery() throws IOException {
        String dbAliasFile = System.getProperty("jSqlPlus.dbAliasses");
        if (dbAliasFile == null || dbAliasFile.length() == 0) {
            throw new IOException("No alias file supplied, use System property "
                    + "jSqlPlus.dbAliasses");
        }
        dbAliasses.load(new FileInputStream(dbAliasFile));
    }

    public String[][] execute(String connectString, String query) throws SQLException, ClassNotFoundException {
        return query(connect(connectString), query);
    }

    private Connection connect(String connectString) throws SQLException, ClassNotFoundException {
        // username:passwor@TNS
        String username = connectString.split(":")[0];
        String password = connectString.split(":")[1].split("@")[0];
        String url = parseConnectString(connectString.split("@")[1]);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    private String[][] query(Connection conn, String query) throws SQLException {
        CallableStatement ps = conn.prepareCall(query);
        ResultSet rs = ps.executeQuery();
        return parseResult(rs);
    }

    private String[][] parseResult(ResultSet rs) throws SQLException {
        ArrayList<String[]> result = new ArrayList<String[]>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        String[] cols = null;
        while (rs.next()) {
            cols = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                cols[i] = rs.getString(i + 1);
            }
            result.add(cols);
        }
        return result.toArray(new String[0][0]);
    }

    private String parseConnectString(String alias) throws ClassNotFoundException {
        String driver = dbAliasses.getProperty(alias).split("\\)\\(")[0].substring(1);
        String url = dbAliasses.getProperty(alias).split("\\)\\(")[1].substring(0, dbAliasses.getProperty(alias).split("\\)\\(")[1].length() - 1);
        Class.forName(driver);
        return url;
    }
}
