package matricula;

import util.ConnectionFactoryMatricula;
import java.sql.*;

public class MatriculaDAO {

    public void matricular(int alunoId, String nomeAluno,
                           int cursoId, String nomeCurso) {

        String sql = "INSERT INTO matricula(aluno_id, nome_aluno, curso_id, nome_curso) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactoryMatricula.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setString(2, nomeAluno);
            stmt.setInt(3, cursoId);
            stmt.setString(4, nomeCurso);

            stmt.executeUpdate();

            if (!alunoDAO.alunoExiste(alunoId)) {
                System.out.println("Erro: Aluno não existe!");
                return;
            }

            if (!cursoDAO.existe(cursoId)) {
                System.out.println("Erro: Curso não existe!");
                return;
            }

            if (!alunoDAO.alunoEstaAtivo(alunoId)) {
                System.out.println("Erro: Aluno inativo, não pode ser matriculado!");
                return;
            }

            if (existeMatriculaAtiva(alunoId, cursoId)) {
                System.out.println("O aluno já possui uma matrícula ativa neste curso.");
                return;
            }

            if (existemCursosNoMesmoHorario(alunoId, cursoId)) {
                System.out.println("O aluno já possui uma matrícula ativa neste curso.");
                return;
            }

            System.out.println("Matrícula realizada!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existeMatriculaAtiva(int alunoId, int cursoId){
        String sql = "SELECT COUNT(id) FROM matricula WHERE aluno_id = ? and curso_id = ? and status = 'ATIVO'";
        //criar campo status
        try(Connection conn = ConnectionFactoryMatricula.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            stmt.setInt(1, alunoId);
            stmt.setInt(2, cursoId);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs > 0){
                    return true;
                }else{
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existemCursosNoMesmoHorario(int alunoId, int cursoId){
        
        ResultSet alunos = alunoDAO.listar();
        ResultSet cursos = cursoDAO.listar();

        String sql = "SELECT *
        FROM matricula m 
        WHERE m.aluno_id = ?";

        try(Connection conn = ConnectionFactoryMatricula.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            stmt.setInt(1, alunoId);

            try(ResultSet rs = stmt.executeQuery()){
                //for(int i = 0 ; i >= alunos.size(); i++);
                if(rs.next()){
                    return true;
                }else{
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listar() {

        String sql = "SELECT * FROM matricula";

        try (Connection conn = ConnectionFactoryMatricula.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                    rs.getString("nome_aluno") +
                    " → " +
                    rs.getString("nome_curso")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}