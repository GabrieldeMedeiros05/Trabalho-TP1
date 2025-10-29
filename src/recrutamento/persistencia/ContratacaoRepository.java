package recrutamento.persistencia;

import recrutamento.dominio.Contratacao;
import java.util.*;

public interface ContratacaoRepository {
    Contratacao salvar(Contratacao c);
    Optional<Contratacao> buscarPorId(String id);
    List<Contratacao> listarTodas();
}
