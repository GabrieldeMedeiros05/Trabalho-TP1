package recrutamento.servico;

import recrutamento.dominio.Contratacao;
import recrutamento.dominio.Entrevista;
import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.dto.FuncionarioDTO;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.persistencia.ContratacaoRepository;
import recrutamento.persistencia.EntrevistaRepository;
import recrutamento.persistencia.VagaRepository;
import recrutamento.interfaces.ICandidaturaService;
import recrutamento.interfaces.IFinanceiroService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio do módulo Recrutamento.
 * Foca em:
 *  - Vagas
 *  - Entrevistas
 *  - Contratações
 * Consulta candidaturas via ICandidaturaService
 * e envia dados de funcionário para o módulo Financeiro via IFinanceiroService.
 */
public class RecrutamentoService {

    private final VagaRepository vagaRepo;
    private final EntrevistaRepository entRepo;
    private final ContratacaoRepository contRepo;

    private final ICandidaturaService candidaturaService;
    private final IFinanceiroService financeiroService;

    // Simulação de sessão (até integração com módulo Administração)
    private String usuarioCpf;
    private String perfil; // "GESTOR" ou "RECRUTADOR"

    public RecrutamentoService(VagaRepository vagaRepo,
                               EntrevistaRepository entRepo,
                               ContratacaoRepository contRepo,
                               ICandidaturaService candidaturaService,
                               IFinanceiroService financeiroService) {
        this.vagaRepo = vagaRepo;
        this.entRepo = entRepo;
        this.contRepo = contRepo;
        this.candidaturaService = candidaturaService;
        this.financeiroService = financeiroService;
    }

    //====================================================
    // CONTROLE SIMPLIFICADO DE SESSÃO
    //====================================================

    public void logarComoGestor(String cpf) {
        this.usuarioCpf = cpf;
        this.perfil = "GESTOR";
    }

    public void logarComoRecrutador(String cpf) {
        this.usuarioCpf = cpf;
        this.perfil = "RECRUTADOR";
    }

    private void exigirGestor() {
        if (!"GESTOR".equals(perfil)) {
            throw new AutorizacaoException("Ação permitida apenas para Gestores.");
        }
    }

    private void exigirRecrutadorDaVaga(Vaga v) {
        boolean ehRecrutador = "RECRUTADOR".equals(perfil);
        boolean responsavel = Objects.equals(v.getRecrutadorResponsavelCpf(), usuarioCpf);
        if (!ehRecrutador || !responsavel) {
            throw new AutorizacaoException("Acesso negado: recrutador não responsável por esta vaga.");
        }
    }

    //====================================================
    // VAGAS
    //====================================================

    /**
     * Cria uma nova vaga (somente gestor).
     */
    public Vaga criarVaga(String cargo,
                          String departamento,
                          double salarioBase,
                          String requisitos,
                          RegimeContratacao regime) {
        exigirGestor();
        String id = UUID.randomUUID().toString();
        Vaga v = new Vaga(id, cargo, departamento, salarioBase, requisitos, regime, usuarioCpf);
        return vagaRepo.salvar(v);
    }

    /**
     * Atribui ou altera o recrutador responsável por uma vaga (somente gestor).
     */
    public void atribuirRecrutador(String vagaId, String recrutadorCpf) {
        exigirGestor();

        Vaga v = vagaRepo.buscarPorId(vagaId)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        if (recrutadorCpf == null || recrutadorCpf.isBlank()) {
            v.setRecrutadorResponsavelCpf(null);
        } else {
            // Aqui poderia ter validação se o recrutador existe no sistema
            v.setRecrutadorResponsavelCpf(recrutadorCpf);
        }

        vagaRepo.salvar(v);
    }

    /**
     * Edita uma vaga existente (somente gestor).
     */
    public Vaga editarVaga(String id,
                           String cargo,
                           String departamento,
                           Double salarioBase,
                           String requisitos,
                           RegimeContratacao regime,
                           StatusVaga status) {
        exigirGestor();

        Vaga v = vagaRepo.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        if (cargo != null) {
            v.setCargo(cargo);
        }
        if (departamento != null) {
            v.setDepartamento(departamento);
        }
        if (salarioBase != null) {
            v.setSalarioBase(salarioBase);
        }
        if (requisitos != null) {
            v.setRequisitos(requisitos);
        }
        if (regime != null) {
            v.setRegime(regime);
        }
        if (status != null) {
            if (status == StatusVaga.FECHADA) {
                v.fechar();
            }
            // Se quiser permitir reabrir, pode criar lógica aqui
        }

        return vagaRepo.salvar(v);
    }

    /**
     * Exclui uma vaga (somente gestor e se não houver candidaturas).
     */
    public void excluirVaga(String id) {
        exigirGestor();

        Vaga v = vagaRepo.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        List<CandidaturaDTO> candidaturas = candidaturaService.listarPorVaga(id);
        if (!candidaturas.isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir uma vaga com candidaturas registradas.");
        }

        vagaRepo.remover(id);
    }

    /**
     * Lista todas as vagas cadastradas.
     */
    public List<Vaga> listarVagas() {
        return vagaRepo.listarTodos();
    }

    /**
     * Filtra vagas conforme múltiplos parâmetros (incluindo faixa de data).
     * Parâmetros nulos são ignorados.
     */
    public List<Vaga> filtrarVagas(String cargo,
                                   String depto,
                                   StatusVaga status,
                                   RegimeContratacao regime,
                                   Double faixaMin,
                                   Double faixaMax,
                                   LocalDate dataMin,
                                   LocalDate dataMax) {

        List<Vaga> base = vagaRepo.filtrar(cargo, depto, status, regime, faixaMin, faixaMax);

        if (dataMin != null || dataMax != null) {
            base = base.stream()
                    .filter(v -> {
                        LocalDate d = v.getDataAbertura();
                        boolean ok = true;
                        if (dataMin != null && d.isBefore(dataMin)) ok = false;
                        if (dataMax != null && d.isAfter(dataMax)) ok = false;
                        return ok;
                    })
                    .collect(Collectors.toList());
        }

        return base;
    }

    //====================================================
    // ENTREVISTAS
    //====================================================

    /**
     * Agenda uma entrevista para uma candidatura existente.
     * Recrutador apenas da vaga correspondente pode agendar.
     */
    public Entrevista agendarEntrevista(String candidaturaId,
                                        LocalDateTime dataHora,
                                        String avaliador) {

        CandidaturaDTO c = candidaturaService.buscarPorId(candidaturaId);
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        exigirRecrutadorDaVaga(v);

        Entrevista e = new Entrevista(
                UUID.randomUUID().toString(),
                candidaturaId,
                dataHora,
                avaliador
        );

        return entRepo.salvar(e);
    }

    /**
     * Lista entrevistas de uma candidatura.
     */
    public List<Entrevista> listarEntrevistasDaCandidatura(String candidaturaId) {
        return entRepo.listarPorCandidatura(candidaturaId);
    }

    //====================================================
    // CONTRATAÇÕES
    //====================================================

    /**
     * Recrutador solicita contratação de uma candidatura aprovada.
     * Verifica:
     *  - se a candidatura existe
     *  - se a vaga existe
     *  - se o recrutador é responsável pela vaga
     *  - se status da candidatura é APROVADO
     *  - se existe ao menos uma entrevista registrada
     */
    public Contratacao solicitarContratacao(String candidaturaId,
                                            RegimeContratacao regime) {

        CandidaturaDTO c = candidaturaService.buscarPorId(candidaturaId);
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        exigirRecrutadorDaVaga(v);

        if (!"APROVADO".equalsIgnoreCase(c.getStatus())) {
            throw new RegraNegocioException("Somente candidaturas aprovadas podem ser contratadas.");
        }

        boolean temEntrevista = !entRepo.listarPorCandidatura(candidaturaId).isEmpty();
        if (!temEntrevista) {
            throw new RegraNegocioException("É obrigatória pelo menos uma entrevista antes da contratação.");
        }

        Contratacao ct = new Contratacao(
                UUID.randomUUID().toString(),
                candidaturaId,
                regime
        );

        return contRepo.salvar(ct);
    }

    /**
     * Gestor autoriza a contratação.
     */
    public Contratacao autorizarContratacao(String contratacaoId) {
        exigirGestor();

        Contratacao ct = contRepo.buscarPorId(contratacaoId)
                .orElseThrow(() -> new RegraNegocioException("Contratação não encontrada."));

        ct.autorizar();
        return contRepo.salvar(ct);
    }

    /**
     * Efetiva a contratação:
     *  - verifica autorização
     *  - cadastra funcionário via módulo Financeiro
     *  - fecha a vaga
     */
    public void efetivarContratacao(String contratacaoId) {
        Contratacao ct = contRepo.buscarPorId(contratacaoId)
                .orElseThrow(() -> new RegraNegocioException("Contratação não encontrada."));

        CandidaturaDTO c = candidaturaService.buscarPorId(ct.getCandidaturaId());
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        exigirRecrutadorDaVaga(v);

        if (ct.getDataAutorizacao() == null) {
            throw new RegraNegocioException("A contratação precisa ser autorizada pelo gestor antes da efetivação.");
        }

        // Monta DTO para o módulo Financeiro
        FuncionarioDTO func = new FuncionarioDTO(
                c.getCandidatoCpf(),
                v.getCargo(),
                v.getSalarioBase(),
                ct.getRegime()
        );

        // Chama serviço do módulo Financeiro
        financeiroService.cadastrarFuncionario(func);

        // Também mantém a simulação em log, se quiser
        System.out.println("[FINANCEIRO] Novo funcionário cadastrado: CPF " + func.getCpf()
                + " | Cargo: " + func.getCargo()
                + " | Salário: " + func.getSalarioBase()
                + " | Regime: " + func.getRegime());

        ct.marcarEfetivada();
        contRepo.salvar(ct);

        v.fechar();
        vagaRepo.salvar(v);
    }

    //====================================================
    // CONSULTAS DE APOIO / RELATÓRIOS
    //====================================================

    /**
     * Lista candidaturas de uma vaga usando o serviço de Candidatura.
     * Apenas leitura, sem manipular domínio de Candidatura.
     */
    public List<CandidaturaDTO> listarCandidaturasDaVaga(String vagaId) {
        return candidaturaService.listarPorVaga(vagaId);
    }

    /**
     * Retorna um resumo numérico do pipeline de uma vaga (status das candidaturas).
     */
    public String pipelineResumo(String vagaId) {
        List<CandidaturaDTO> cands = candidaturaService.listarPorVaga(vagaId);

        Map<String, Long> cont = cands.stream()
                .collect(Collectors.groupingBy(
                        CandidaturaDTO::getStatus,
                        Collectors.counting()
                ));

        return "Pipeline da Vaga " + vagaId + " → " + cont;
    }
}
