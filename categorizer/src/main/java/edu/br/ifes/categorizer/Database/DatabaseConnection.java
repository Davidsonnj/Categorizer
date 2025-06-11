package edu.br.ifes.categorizer.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    public static final String URL = "jdbc:postgresql://localhost:5433/bancoemails";
    public static final String USER = "davidson";
    public static final String PASSWORD = "davidson123";

    private DatabaseConnection() {
        // Evitar instancias ;-;
    }

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Conexão com o banco de dados fechada com sucesso!");
            }
        }catch (SQLException e){
            System.out.println("Erro ao fechar a conexão com o banco: "+ e.getMessage());
        }
    }
}
