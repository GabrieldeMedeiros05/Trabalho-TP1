package recrutamento.servico;

import Seguranca.dominio.Usuario;
import recrutamento.dominio.Contratacao;
import recrutamento.dominio.Entrevista;
import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.dto.FuncionarioDTO;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.interfaces.ICandidaturaService;
import recrutamento.interfaces.IFinanceiroService;
import recrutamento.persistencia.ContratacaoRepository;
import recrutamento.persistencia.EntrevistaRepository;
import recrutamento.persistencia.VagaRepository;

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

    // Dados do usuário logado (via módulo Segurança)
    private String usuarioCpf;   // CPF do usuário logado
    private String perfil;       // "GESTOR", "RECRUTADOR" ou "OUTRO"

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
    // INTEGRAÇÃO COM MÓDULO DE SEGURANÇA
    //====================================================

    /**
     * Configura o usuário logado a partir do módulo de Segurança.
     * Usado pelo MenuPrincipal antes de abrir o módulo Recrutamento.
     */
    public void configurarUsuarioLogado(Usuario usuario) {
        if (usuario == null) {
            this.usuarioCpf = null;
            this.perfil = null;
            return;
        }

        // Ajuste conforme os getters reais de Usuario
        this.usuarioCpf = usuario.getCpf_cnpj();

        String tipo = usuario.getTipo(); // ex.: "GESTOR_RH", "RECRUTADOR_RH"
        if (tipo != null) {
            String t = tipo.toUpperCase();
            if (t.contains("GESTOR")) {
                this.perfil = "GESTOR";
            } else if (t.contains("RECRUTADOR")) {
                this.perfil = "RECRUTADOR";
            } else {
                this.perfil = "OUTRO";
            }
        } else {
            this.perfil = "OUTRO";
        }
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
        String codigo = gerarCodigoAmigavel();

        Vaga v = new Vaga(
                id,
                cargo,
                departamento,
                salarioBase,
                requisitos,
                regime,
                usuarioCpf,   // gestorResponsavelCpf
                codigo
        );

        return vagaRepo.salvar(v);
    }

    /**
     * Gera um código numérico “amigável” de 6 dígitos para a vaga.
     * Obs.: simples, pode ter colisão. Em um sistema real, validaríamos unicidade.
     */
    private String gerarCodigoAmigavel() {
        int numero = new Random().nextInt(900000) + 100000; // de 100000 a 999999
        return String.valueOf(numero);
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
     * Lista entrevistas de uma candidatura.
     */
    public List<Entrevista> listarEntrevistasDaCandidatura(String candidaturaId) {
        return entRepo.listarPorCandidatura(candidaturaId);
    }

    /**
     * Retorna uma entrevista pelo ID ou null se não encontrar.
     */
    public Entrevista buscarEntrevistaPorId(String entrevistaId) {
        return entRepo.buscarPorId(entrevistaId).orElse(null);
    }

    /**
     * Agenda uma entrevista (versão curta, compatibilidade).
     */
    public Entrevista agendarEntrevista(String candidaturaId,
                                        LocalDateTime dataHora,
                                        String avaliador) {
        return agendarEntrevista(candidaturaId, dataHora, avaliador, 0.0, null);
    }

    /**
     * Agenda uma entrevista (compatível), com nota mas sem parecer explícito.
     */
    public Entrevista agendarEntrevista(String candidaturaId,
                                        LocalDateTime dataHora,
                                        String avaliador,
                                        double nota) {
        return agendarEntrevista(candidaturaId, dataHora, avaliador, nota, null);
    }

    /**
     * Agenda uma entrevista (versão completa), com nota e parecer.
     * Permitido apenas para o recrutador responsável pela vaga.
     */
    public Entrevista agendarEntrevista(String candidaturaId,
                                        LocalDateTime dataHora,
                                        String avaliador,
                                        double nota,
                                        String parecer) {

        CandidaturaDTO c = candidaturaService.buscarPorId(candidaturaId);
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = buscarVagaDaCandidatura(c);
        if (v == null) {
            throw new RegraNegocioException("Vaga não encontrada para a candidatura selecionada.");
        }

        // Apenas o recrutador responsável pode agendar
        exigirRecrutadorDaVaga(v);

        Entrevista e = new Entrevista(
                UUID.randomUUID().toString(),
                candidaturaId,
                dataHora,
                avaliador
        );

        // registra nota + parecer
        e.registrarAvaliacao(nota, parecer);

        return entRepo.salvar(e);
    }

    /**
     * Atualiza os dados de uma entrevista existente (data, avaliador, nota).
     * Versão de compatibilidade (sem parecer explícito).
     */
    public Entrevista atualizarEntrevista(String entrevistaId,
                                          LocalDateTime dataHora,
                                          String avaliador,
                                          double nota) {
        return atualizarEntrevista(entrevistaId, dataHora, avaliador, nota, null);
    }

    /**
     * Atualiza os dados de uma entrevista existente (data, avaliador, nota, parecer).
     */
    public Entrevista atualizarEntrevista(String entrevistaId,
                                          LocalDateTime dataHora,
                                          String avaliador,
                                          double nota,
                                          String parecer) {

        Entrevista e = entRepo.buscarPorId(entrevistaId)
                .orElseThrow(() -> new RegraNegocioException("Entrevista não encontrada."));

        // Garantir que o recrutador é responsável pela vaga daquela candidatura
        CandidaturaDTO c = candidaturaService.buscarPorId(e.getCandidaturaId());
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = buscarVagaDaCandidatura(c);
        if (v == null) {
            throw new RegraNegocioException("Vaga não encontrada para a candidatura selecionada.");
        }

        exigirRecrutadorDaVaga(v);

        e.setDataHora(dataHora);
        e.setAvaliador(avaliador);
        e.registrarAvaliacao(nota, parecer);

        return entRepo.salvar(e);
    }

    /**
     * Exclui uma entrevista (somente recrutador responsável pela vaga).
     */
    public void excluirEntrevista(String entrevistaId) {
        Entrevista e = entRepo.buscarPorId(entrevistaId)
                .orElseThrow(() -> new RegraNegocioException("Entrevista não encontrada."));

        CandidaturaDTO c = candidaturaService.buscarPorId(e.getCandidaturaId());
        if (c == null) {
            throw new RegraNegocioException("Candidatura não encontrada.");
        }

        Vaga v = buscarVagaDaCandidatura(c);
        if (v == null) {
            throw new RegraNegocioException("Vaga não encontrada para a candidatura selecionada.");
        }

        exigirRecrutadorDaVaga(v);

        entRepo.remover(entrevistaId);
    }

    //====================================================
    // AUXÍLIO PARA TELA DE ENTREVISTAS
    //====================================================
    /**
     * Lista candidaturas elegíveis para solicitação de contratação:
     * - status APROVADO
     * - possuem ao menos uma entrevista com parecer "Solicitar contratação"
     * - se perfil for RECRUTADOR, apenas vagas em que ele é o recrutador responsável
     */
    public List<CandidaturaDTO> listarCandidaturasParaSolicitarContratacao() {
        List<CandidaturaDTO> todas = candidaturaService.listarTodas();
        List<CandidaturaDTO> resultado = new ArrayList<>();

        for (CandidaturaDTO c : todas) {
            // Só faz sentido contratar quem está aprovado
            if (!"APROVADO".equalsIgnoreCase(c.getStatus())) {
                continue;
            }

            Vaga v = buscarVagaDaCandidatura(c);
            if (v == null) {
                continue; // candidatura aponta pra vaga inexistente
            }

            // Se for RECRUTADOR, só vê vagas em que ele é o recrutador responsável
            if ("RECRUTADOR".equals(perfil)) {
                if (!Objects.equals(v.getRecrutadorResponsavelCpf(), usuarioCpf)) {
                    continue;
                }
            }

            // Verifica se há entrevista com parecer "Solicitar contratação"
            List<Entrevista> entrevistas = entRepo.listarPorCandidatura(c.getId());
            boolean temParecerSolic = entrevistas.stream().anyMatch(e -> {
                String p = e.getParecer();
                if (p == null) return false;
                String pNorm = p.trim().toLowerCase();
                return pNorm.equals("solicitar contratação")
                        || pNorm.equals("solicitar contratacao");
            });

            if (temParecerSolic) {
                resultado.add(c);
            }
        }

        return resultado;
    }


    /**
     * Lista candidaturas que podem aparecer na tela de entrevistas.
     * Exemplo: todas, ou só as da vaga do recrutador.
     */
    public List<CandidaturaDTO> listarCandidaturasParaEntrevista() {
        List<CandidaturaDTO> todas = candidaturaService.listarTodas();

        // 1) mantém só candidaturas que têm uma vaga correspondente
        List<CandidaturaDTO> comVaga = todas.stream()
                .filter(c -> buscarVagaDaCandidatura(c) != null)
                .collect(Collectors.toList());

        // 2) Se for RECRUTADOR, filtra apenas vagas sob sua responsabilidade
        if ("RECRUTADOR".equals(perfil)) {
            return comVaga.stream()
                    .filter(c -> {
                        Vaga v = buscarVagaDaCandidatura(c);
                        if (v == null) return false;
                        String resp = v.getRecrutadorResponsavelCpf();
                        // inclui vagas sem recrutador OU com recrutador = usuário logado
                        return resp == null || Objects.equals(resp, usuarioCpf);
                    })
                    .collect(Collectors.toList());
        }

        // 3) Gestor (e outros perfis) veem todas as candidaturas com vaga válida
        return comVaga;
    }

    /**
     * Tenta encontrar a Vaga associada a uma candidatura.
     * Primeiro tenta por ID (UUID), depois tenta interpretar vagaId como "codigo" da vaga.
     */
    private Vaga buscarVagaDaCandidatura(CandidaturaDTO c) {
        if (c == null) return null;

        // 1) tenta por ID (UUID interno da vaga)
        Optional<Vaga> opt = vagaRepo.buscarPorId(c.getVagaId());
        if (opt.isPresent()) {
            return opt.get();
        }

        // 2) fallback: tenta usar vagaId como "codigo amigável" (ex.: 4669066)
        return vagaRepo.listarTodos().stream()
                .filter(v -> c.getVagaId().equals(v.getCodigo()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Monta um rótulo amigável para exibir a candidatura na combobox:
     * "CPF - Cargo da vaga (CODIGO)"
     */
    public String montarRotuloCandidatura(CandidaturaDTO c) {
        if (c == null) {
            return "";
        }

        String cpf = c.getCandidatoCpf();
        String cargo = "";
        String codigoVaga = "";

        Vaga v = buscarVagaDaCandidatura(c);
        if (v != null) {
            cargo = v.getCargo();
            codigoVaga = v.getCodigo(); // se quiser mostrar o código amigável
        }

        StringBuilder sb = new StringBuilder();
        sb.append(cpf);
        if (!cargo.isBlank()) {
            sb.append(" - ").append(cargo);
        } else {
            sb.append(" - Vaga ").append(c.getVagaId());
        }
        if (codigoVaga != null && !codigoVaga.isBlank()) {
            sb.append(" (").append(codigoVaga).append(")");
        }

        return sb.toString();
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

        Vaga v = buscarVagaDaCandidatura(c);
        if (v == null) {
            throw new RegraNegocioException("Vaga não encontrada.");
        }

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

        Vaga v = buscarVagaDaCandidatura(c);
        if (v == null) {
            throw new RegraNegocioException("Vaga não encontrada.");
        }

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
