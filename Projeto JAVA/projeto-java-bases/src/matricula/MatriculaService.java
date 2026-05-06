package com.instituicao.matricula.service;

import com.instituicao.matricula.mock.AlunoMock;
import com.instituicao.matricula.mock.CursoMock;
// import com.instituicao.matricula.mock.SistemasExternosMock;
import com.instituicao.matricula.model.CursoAluno;
import com.instituicao.matricula.repository.CursoAlunoRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MatriculaService {

    private final CursoAlunoRepositoryImpl repository;
    // private final SistemasExternosMock sistemasExternos;
    
    public MatriculaService(CursoAlunoRepositoryImpl repository, 
    ) {
        this.repository = repository;
        this.sistemasExternos = sistemasExternos;
    }

    /*
     * Busca a matrícula pelo número, obtém o aluno e cancela corretamente.
     */
    public CursoAluno cancelarMatriculaPorNumero(String numeroMatricula) throws Exception {
        // Busca a matrícula pelo número
        Optional<CursoAluno> matriculaOpt = repositoryBuscarPorNumeroMatricula(numeroMatricula);
        if (!matriculaOpt.isPresent()) {
            throw new Exception("Matrícula não encontrada pelo número informado.");
        }
        CursoAluno matricula = matriculaOpt.get();
        if (!matricula.isStatus()) {
            throw new Exception("Esta matrícula já está cancelada.");
        }
        // Altera status
        matricula.setStatus(false);
        CursoAluno matriculaCancelada = repository.atualizar(matricula);
        // Libera vaga no curso
        sistemasExternos.buscarCurso(matricula.getIdCurso()).ifPresent(curso -> curso.removerAluno());
        return matriculaCancelada;
    }

    // Método auxiliar para buscar matrícula pelo número (String)
    private Optional<CursoAluno> repositoryBuscarPorNumeroMatricula(String numeroMatricula) {
        // Busca em todas as matrículas salvas
        // Como não existe método na interface, faz busca manual
        if (repository instanceof com.instituicao.matricula.repository.CursoAlunoRepositoryImpl) {
            com.instituicao.matricula.repository.CursoAlunoRepositoryImpl impl =
                (com.instituicao.matricula.repository.CursoAlunoRepositoryImpl) repository;
            return impl.buscarPorNumeroMatricula(numeroMatricula);
        }
        // Se não for a implementação esperada, retorna vazio
        return Optional.empty();
    }

    public CursoAluno registrarMatricula(Long idAluno, Long idCurso) throws Exception {
        
        // 1. Busca os dados externos
       // Optional<AlunoMock> alunoOpt =  sistemasExternos.buscarAluno(idAluno);
        if (!alunoOpt.isPresent()) {
            throw new Exception("Aluno não encontrado no sistema externo.");
        }
        
        AlunoMock aluno = alunoOpt.get();

        Optional<CursoMock> cursoOpt = sistemasExternos.buscarCurso(idCurso);
        if (!cursoOpt.isPresent()) {
            throw new Exception("Curso não encontrado no sistema externo.");
        }
        CursoMock novoCurso = cursoOpt.get();

        // Regra: Aluno inativo não pode se matricular
        if (!aluno.isAtivo()) {
            throw new Exception("Matrícula negada: O aluno " + aluno.getNome() + " está inativo.");
        }

        // Regra: Um aluno não pode estar matriculado duas vezes no mesmo curso
        if (repository.existeMatriculaAtiva(idAluno, idCurso)) {
            throw new Exception("O aluno já possui uma matrícula ativa neste curso.");
        }

        // Regra: Verificar lotação do curso
        if (novoCurso.isLotado()) {
            throw new Exception("O curso " + novoCurso.getNome() + " já atingiu o número máximo de " + novoCurso.getVagasMaximas() + " alunos.");
        }

        // Regra: Verificar conflito de horários
        // Busca todas as matrículas ativas do aluno
        List<CursoAluno> matriculasAtivas = repository.buscarMatriculasAtivasPorAluno(idAluno);
        for(CursoAluno matricula : matriculasAtivas) {
            // Busca os dados do curso que ele já está matriculado
            CursoMock cursoExistente = sistemasExternos.buscarCurso(matricula.getIdCurso()).orElse(null);
            if (cursoExistente != null && cursoExistente.getHorario().equals(novoCurso.getHorario())) {
                throw new Exception("Conflito de horários com o curso: " + cursoExistente.getNome() + " (Horário: " + novoCurso.getHorario() + ")");
            }
        }

        // Se passou em todas as validações, criamos o Model
        CursoAluno novaMatricula = new CursoAluno();
        novaMatricula.setIdAluno(idAluno);
        novaMatricula.setIdCurso(idCurso);
        novaMatricula.setDataMatricula(LocalDateTime.now());
        novaMatricula.setStatus(true);
        novaMatricula.setNumeroMatricula(gerarNumeroMatricula(idAluno, idCurso));

        // Integração: Ocupar a vaga no sistema externo de cursos
        novoCurso.adicionarAluno();

        return repository.salvar(novaMatricula);
    }


    public List<CursoAluno> consultarAlunosPorCurso(Long idCurso) {
        return repository.buscarMatriculasAtivasPorCurso(idCurso);
    }

    private String gerarNumeroMatricula(Long idAluno, Long idCurso) {
        return idCurso + "-" + idAluno + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
