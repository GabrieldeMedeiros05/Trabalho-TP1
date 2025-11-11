package Candidatura.persistencia;

import Candidatura.dominio.Candidato;
import java.util.List;
import java.util.Optional;

public interface CandidatoRepository {
    Candidato salvar(Candidato candidato);
    Optional<Candidato> buscarPorCpf(String cpf);
    List<Candidato> listarTodos();
    boolean deletarPorCpf(String cpf);
}