package recrutamento.persistencia;

import recrutamento.dominio.Entrevista;
import java.util.*;

public interface EntrevistaRepository {
    Entrevista salvar(Entrevista e);
    List<Entrevista> listarPorCandidatura(String candidaturaId);
}
