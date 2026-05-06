package curso;

import util.ConnectionFactoryCurso;
import java.sql.*;

public class CursoDAO {

    public void inserir(String nome, String horario) {

        String sql = "INSERT INTO curso(nome, horario) VALUES (?, ?)";

        try (Connection conn = ConnectionFactoryCurso.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, horario);

            stmt.executeUpdate();

            System.out.println("Curso inserido!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet listar() {

        String sql = "SELECT * FROM curso";

        try (Connection conn = ConnectionFactoryCurso.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                return rs;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}