package recrutamento.persistencia;

import recrutamento.dominio.Candidatura;
import java.util.*;

public interface CandidaturaRepository {
    Candidatura salvar(Candidatura c);
    Optional<Candidatura> buscarPorId(String id);
    List<Candidatura> listarPorVaga(String vagaId);
    List<Candidatura> listarTodas();
}
