/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author apantoja
 */
public class ConexionDAO {

    protected Connection conn;

    private void conexionSCPRO() throws ClassNotFoundException {
        try {

            String USER = "di";
            String PASSWROD = "";
            String URL = "jdbc:sqlserver://172.24.144.79;databaseName=SISMOON";
            String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWROD);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection abrirConexionSCPRO() throws ClassNotFoundException {
        if (conn == null) {
            conexionSCPRO();
        }
        return conn;
    }

    public void cerrarConexionSCPRO() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //
    private void conexionDDEE() throws ClassNotFoundException {
        try {

            String USER = "User_sde";
            String PASSWROD = "";
            String URL = "jdbc:sqlserver://172.24.148.5;databaseName=SDE";
            String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWROD);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection abrirConexionDDEE() throws ClassNotFoundException {
        if (conn == null) {
            conexionDDEE();
        }
        return conn;
    }

    public void cerrarConexionDDEE() {
        try {
            if (conn != null && !conn.isClosed()) {
                //System.out.println("CERRO CONEXION RADAR");
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void conexionSDR() throws ClassNotFoundException {
        try {

            String USER = "User_SistemasDI";
            String PASSWROD = "";
            String URL = "jdbc:sqlserver://172.24.144.18;databaseName=Sistemas_DI";
            String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWROD);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection abrirConexionSDR() throws ClassNotFoundException {
        if (conn == null) {
            conexionSDR();
        }
        return conn;
    }

    public void cerrarConexionSDR() {
        try {
            if (conn != null && !conn.isClosed()) {
                //System.out.println("CERRO CONEXION RADAR");
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet scripSelect(String plsql) throws SQLException {
        ResultSet rset = null;
        Statement stmt = conn.createStatement();
        rset = stmt.executeQuery(plsql);

        return rset;

    }

    public int scripUpdate(String plsql) throws SQLException {
        int rset = 0;
        Statement stmt = conn.createStatement();
        rset = stmt.executeUpdate(plsql);

        return rset;

    }

    private void conexionAPEMAMAZON() throws ClassNotFoundException {
        try {

            String USER = "user_apem";
            String PASSWROD = "";
            String URL = "jdbc:postgresql://postgresql.cddvfkul0ldi.us-east-1.rds.amazonaws.com/apem_home";
            String DRIVER = "org.postgresql.Driver";
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWROD);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection abrirConexionAPEMAMAZON() throws ClassNotFoundException {
        if (conn == null) {
            conexionAPEMAMAZON();
        }
        return conn;
    }

    public void cerrarConexionAPEMAMAZON() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
