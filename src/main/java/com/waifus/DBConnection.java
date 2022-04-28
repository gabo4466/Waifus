package com.waifus;

import java.sql.*;

public class DBConnection {

    private static Connection connection = null;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3360/waifus";
    private DBConnection() {
    }

    /**
     * Metodo que establece la conexion con la base de datos
     * @return Conexion con la base de datos
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null){
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(JDBC_URL, "root", "1234");
        }
        return connection;
    }
}
