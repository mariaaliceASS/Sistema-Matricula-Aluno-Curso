import aluno.AlunoDAO;
import curso.CursoDAO;
import matricula.matriculaService;

public class Main {

    public static void main(String[] args) {

        AlunoDAO alunoDAO = new AlunoDAO();
        CursoDAO cursoDAO = new CursoDAO();
        MatriculaService matriculaService = new MatriculaService()

        alunoDAO.inserir("João");
        cursoDAO.inserir("Java", "19h");

        System.out.println("ALUNOS:");
        alunoDAO.listar();

        System.out.println("\nCURSOS:");
        cursoDAO.listar();

        matriculaService.registrarMatricula(1, "", 1, "");

        System.out.println("\nMATRÍCULAS:");
        matriculaDAO.listar();
    }
}