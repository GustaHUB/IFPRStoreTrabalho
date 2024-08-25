package connection;

import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection connection;

    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/departamentos"; // URL do PostgreSQL
        String user = "postgres"; // Substitua pelo nome de usuário do PostgreSQL
        String pass = "ifpr";

        try {
            Class.forName("org.postgresql.Driver"); // Classe do driver do PostgreSQL
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Não foi possível encerrar a conexão: " + e.getMessage());
        }
    }
}
