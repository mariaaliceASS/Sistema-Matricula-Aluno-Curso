package aluno;

import util.ConnectionFactoryAluno;
import java.sql.*;

public class AlunoDAO {

    public void inserir(String nome) {

        String sql = "INSERT INTO aluno(nome, status) VALUES (?, 'ATIVO')";

        try (Connection conn = ConnectionFactoryAluno.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.executeUpdate();

            System.out.println("Aluno inserido!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet listar() {

        String sql = "SELECT * FROM aluno";

        try (Connection conn = ConnectionFactoryAluno.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                return rs;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean alunoExiste(int id) {
        String sql = "SELECT id FROM aluno WHERE id = ?";

        try (Connection conn = ConnectionFactoryAluno.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
             stmt.setInt(1, id);

             try (ResultSet rs = stmt.executeQuery()) {
                 return rs.next();
             }
        }catch(Exception e){
                 e.printStackTrace();
        }
             return false;
    }


public boolean alunoEstaAtivo(int id){
        String sql = "SELECT id FROM aluno WHERE id = ?";

        try (Connection conn = ConnectionFactoryAluno.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    String status = rs.getString("status");
                    return "ATIVO".equalsIgnoreCase(status);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }
}