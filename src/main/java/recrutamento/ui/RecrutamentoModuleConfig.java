package recrutamento.ui;

import recrutamento.persistencia.*;
import recrutamento.persistencia.csv.*;
import recrutamento.servico.RecrutamentoService;

import java.nio.file.Path;

public class RecrutamentoModuleConfig {

    private static RecrutamentoService instance;

    public static RecrutamentoService service() {
        if (instance == null) {
            VagaRepository vagaRepo = new VagaRepositoryCsv(Path.of("data/vagas.csv"));
            CandidaturaRepository candRepo = new CandidaturaRepositoryCsv(Path.of("data/candidaturas.csv"));
            EntrevistaRepository entRepo = new EntrevistaRepositoryCsv(Path.of("data/entrevistas.csv"));
            ContratacaoRepository contRepo = new ContratacaoRepositoryCsv(Path.of("data/contratacoes.csv"));

            instance = new RecrutamentoService(vagaRepo, candRepo, entRepo, contRepo);
        }
        return instance;
    }
}
