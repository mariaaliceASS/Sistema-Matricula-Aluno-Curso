package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryAluno {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/alunos_db?useUnicode=true&characterEncoding=UTF-8",
                "root",
                ""
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}