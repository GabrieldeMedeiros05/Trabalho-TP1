package recrutamento.persistencia;

import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;

import java.util.List;
import java.util.Optional;

public interface VagaRepository {
    Vaga salvar(Vaga v);
    Optional<Vaga> buscarPorId(String id);
    List<Vaga> listarTodos();
    void remover(String id);

    List<Vaga> filtrar(String cargo, String departamento, StatusVaga status,
                       RegimeContratacao regime, Double faixaMin, Double faixaMax);
}
