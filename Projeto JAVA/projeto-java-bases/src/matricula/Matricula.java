package matricula;

public class Matricula {

    private int alunoId;
    private int cursoId;

    public Matricula(int alunoId, int cursoId) {
        this.alunoId = alunoId;
        this.cursoId = cursoId;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public int getCursoId() {
        return cursoId;
    }
}