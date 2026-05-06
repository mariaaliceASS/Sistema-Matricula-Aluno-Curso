package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryCurso {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cursos_db?useUnicode=true&characterEncoding=UTF-8",
                "root",
                ""
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}