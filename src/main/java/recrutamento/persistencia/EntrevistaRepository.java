package recrutamento.persistencia;

import recrutamento.dominio.Entrevista;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistência para Entrevista.
 */
public interface EntrevistaRepository {

    Entrevista salvar(Entrevista e);

    Optional<Entrevista> buscarPorId(String id);

    List<Entrevista> listarPorCandidatura(String candidaturaId);

    void remover(String id);
}
