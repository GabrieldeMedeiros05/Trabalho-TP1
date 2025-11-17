package Candidatura.persistencia;

import Candidatura.dominio.Candidatura;

import java.util.List;
import java.util.Optional;

public interface CandidaturaRepository {
    Candidatura salvar(Candidatura c);
    Optional<Candidatura> buscarPorId(String id);
    List<Candidatura> listarPorVaga(String vagaId);

    boolean deletarPorCpf(String cpf);

    List<Candidatura> listarTodas();
}