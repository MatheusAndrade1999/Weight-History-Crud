package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String HOSTNAME = "127.0.0.1"; // ou o endereço IP da sua máquina
    private static final int PORT = 3306; // porta padrão do MySQL
    private static final String DATABASE_NAME = "escola";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE_NAME + "?user=" + USER + "&password=" + PASSWORD;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados", e);
        }
    }
}
