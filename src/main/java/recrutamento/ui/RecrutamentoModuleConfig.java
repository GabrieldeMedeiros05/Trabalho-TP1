package recrutamento.ui;

import Candidatura.persistencia.CandidaturaRepository;
import Candidatura.persistencia.csv.CandidaturaRepositoryCsv;
import Financeiro.FinanceiroServiceAdapter;
import recrutamento.integracao.CandidaturaServiceAdapter;
import recrutamento.interfaces.ICandidaturaService;
import recrutamento.interfaces.IFinanceiroService;
import recrutamento.persistencia.ContratacaoRepository;
import recrutamento.persistencia.EntrevistaRepository;
import recrutamento.persistencia.VagaRepository;
import recrutamento.persistencia.csv.ContratacaoRepositoryCsv;
import recrutamento.persistencia.csv.EntrevistaRepositoryCsv;
import recrutamento.persistencia.csv.VagaRepositoryCsv;
import recrutamento.servico.RecrutamentoService;

import java.nio.file.Path;

public class RecrutamentoModuleConfig {

    private static RecrutamentoService instance;

    public static RecrutamentoService service() {
        if (instance == null) {

            // Persistência do módulo Recrutamento
            VagaRepository vagaRepo = new VagaRepositoryCsv(Path.of("data/vagas.csv"));
            EntrevistaRepository entRepo = new EntrevistaRepositoryCsv(Path.of("data/entrevistas.csv"));
            ContratacaoRepository contRepo = new ContratacaoRepositoryCsv(Path.of("data/contratacoes.csv"));

            // Persistência do módulo Candidatura
            CandidaturaRepository candidaturaRepository =
                    new CandidaturaRepositoryCsv(Path.of("data/candidaturas.csv"));

            // Adapter para falar com o módulo Candidatura
            ICandidaturaService candidaturaService =
                    new CandidaturaServiceAdapter(candidaturaRepository);

            // Integração real com o módulo Financeiro
            IFinanceiroService financeiroService =
                    new FinanceiroServiceAdapter();

            // Injeta apenas o que é de Recrutamento + interfaces dos outros módulos
            instance = new RecrutamentoService(
                    vagaRepo,
                    entRepo,
                    contRepo,
                    candidaturaService,
                    financeiroService
            );
        }
        return instance;
    }
}
