package recrutamento.servico;

import recrutamento.dominio.*;
import recrutamento.excecoes.*;
import recrutamento.persistencia.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio do módulo Recrutamento.
 *
 * Permite:
 *  - Criar, editar, excluir, listar e filtrar vagas;
 *  - Gerenciar candidaturas e entrevistas;
 *  - Solicitar e autorizar contratações;
 *  - Efetivar contratações (integração futura com Financeiro);
 *
 * Aplica os princípios de POO:
 *  - Abstração (interfaces de repositório);
 *  - Encapsulamento (atributos privados e acesso controlado);
 *  - Polimorfismo (validações e fluxos conforme o perfil do usuário);
 *  - Herança (planejada para Recrutador → Usuario).
 */
public class RecrutamentoService {

    private final VagaRepository vagaRepo;
    private final CandidaturaRepository candRepo;
    private final EntrevistaRepository entRepo;
    private final ContratacaoRepository contRepo;

    // Simulação de sessão (até integração com módulo Administração)
    private String usuarioCpf;
    private String perfil; // "GESTOR" ou "RECRUTADOR"

    public RecrutamentoService(VagaRepository vagaRepo,
                               CandidaturaRepository candRepo,
                               EntrevistaRepository entRepo,
                               ContratacaoRepository contRepo) {
        this.vagaRepo = vagaRepo;
        this.candRepo = candRepo;
        this.entRepo = entRepo;
        this.contRepo = contRepo;
    }


    //CONTROLE SIMPLIFICADO DE SESSÃO

    public void logarComoGestor(String cpf) { this.usuarioCpf = cpf; this.perfil = "GESTOR"; }
    public void logarComoRecrutador(String cpf) { this.usuarioCpf = cpf; this.perfil = "RECRUTADOR"; }

    private void exigirGestor() {
        if (!"GESTOR".equals(perfil))
            throw new AutorizacaoException("Ação permitida apenas para Gestores.");
    }

    private void exigirRecrutadorDaVaga(Vaga v) {
        if (!"RECRUTADOR".equals(perfil) || !Objects.equals(v.getRecrutadorResponsavelCpf(), usuarioCpf))
            throw new AutorizacaoException("Acesso negado: recrutador não responsável por esta vaga.");
    }

    //VAGAS

    /** Cria uma nova vaga (somente gestor). */
    public Vaga criarVaga(String cargo, String departamento, double salarioBase,
                          String requisitos, RegimeContratacao regime) {
        exigirGestor();
        String id = UUID.randomUUID().toString();
        Vaga v = new Vaga(id, cargo, departamento, salarioBase, requisitos, regime, usuarioCpf);
        return vagaRepo.salvar(v);
    }

    /** Edita uma vaga existente (somente gestor). */
    public Vaga editarVaga(String id, String cargo, String departamento, Double salarioBase,
                           String requisitos, RegimeContratacao regime, StatusVaga status) {
        exigirGestor();
        Vaga v = vagaRepo.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        if (cargo != null) v.setCargo(cargo);
        if (departamento != null) v.setDepartamento(departamento);
        if (salarioBase != null) v.setSalarioBase(salarioBase);
        if (requisitos != null) v.setRequisitos(requisitos);
        if (regime != null) v.setRegime(regime);
        if (status != null) {
            if (status == StatusVaga.FECHADA) v.fechar();
            else {
                // Caso queira reabrir a vaga, pode adicionar lógica adicional.
            }
        }
        return vagaRepo.salvar(v);
    }

    /** Exclui uma vaga (somente gestor e se não houver candidaturas). */
    public void excluirVaga(String id) {
        exigirGestor();
        Vaga v = vagaRepo.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        List<Candidatura> candidaturas = candRepo.listarPorVaga(id);
        if (!candidaturas.isEmpty())
            throw new RegraNegocioException("Não é possível excluir uma vaga com candidaturas registradas.");
        vagaRepo.remover(id);
    }

    /** Lista todas as vagas cadastradas. */
    public List<Vaga> listarVagas() { return vagaRepo.listarTodos(); }

    /**
     * Filtra vagas conforme múltiplos parâmetros (incluindo faixa de data).
     * Parâmetros nulos são ignorados.
     */
    public List<Vaga> filtrarVagas(String cargo, String depto, StatusVaga status,
                                   RegimeContratacao regime, Double faixaMin, Double faixaMax,
                                   LocalDate dataMin, LocalDate dataMax) {
        List<Vaga> base = vagaRepo.filtrar(cargo, depto, status, regime, faixaMin, faixaMax);
        if (dataMin != null || dataMax != null) {
            base = base.stream().filter(v -> {
                LocalDate d = v.getDataAbertura();
                boolean ok = true;
                if (dataMin != null && d.isBefore(dataMin)) ok = false;
                if (dataMax != null && d.isAfter(dataMax)) ok = false;
                return ok;
            }).collect(Collectors.toList());
        }
        return base;
    }

    //CANDIDATURAS


    public Candidatura registrarCandidatura(String vagaId, String candidatoCpf) {
        Vaga v = vagaRepo.buscarPorId(vagaId)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        if (!v.estaAberta())
            throw new RegraNegocioException("Vaga não está aberta.");
        exigirRecrutadorDaVaga(v);

        boolean duplicada = candRepo.listarPorVaga(vagaId).stream()
                .anyMatch(c -> c.getCandidatoCpf().equals(candidatoCpf));
        if (duplicada)
            throw new RegraNegocioException("Candidato já possui candidatura nesta vaga.");

        Candidatura c = new Candidatura(UUID.randomUUID().toString(), vagaId, candidatoCpf);
        return candRepo.salvar(c);
    }

    public Candidatura atualizarStatusCandidatura(String candidaturaId, StatusCandidatura novo) {
        Candidatura c = candRepo.buscarPorId(candidaturaId)
                .orElseThrow(() -> new RegraNegocioException("Candidatura não encontrada."));
        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        exigirRecrutadorDaVaga(v);

        try {
            var f = Candidatura.class.getDeclaredMethod("setStatus", StatusCandidatura.class);
            f.setAccessible(true);
            f.invoke(c, novo);
        } catch (Exception e) {
            throw new RegraNegocioException("Falha ao alterar status da candidatura.");
        }
        return candRepo.salvar(c);
    }

    //ENTREVISTAS

    public Entrevista agendarEntrevista(String candidaturaId, LocalDateTime dataHora, String avaliador) {
        Candidatura c = candRepo.buscarPorId(candidaturaId)
                .orElseThrow(() -> new RegraNegocioException("Candidatura não encontrada."));
        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        exigirRecrutadorDaVaga(v);

        Entrevista e = new Entrevista(UUID.randomUUID().toString(), candidaturaId, dataHora, avaliador);
        return entRepo.salvar(e);
    }


    //CONTRATAÇÕES

    public Contratacao solicitarContratacao(String candidaturaId, RegimeContratacao regime) {
        Candidatura c = candRepo.buscarPorId(candidaturaId)
                .orElseThrow(() -> new RegraNegocioException("Candidatura não encontrada."));
        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        exigirRecrutadorDaVaga(v);

        if (c.getStatus() != StatusCandidatura.APROVADO)
            throw new RegraNegocioException("Somente candidaturas aprovadas podem ser contratadas.");

        boolean temEntrevista = !entRepo.listarPorCandidatura(candidaturaId).isEmpty();
        if (!temEntrevista)
            throw new RegraNegocioException("É obrigatória pelo menos uma entrevista antes da contratação.");

        Contratacao ct = new Contratacao(UUID.randomUUID().toString(), candidaturaId, regime);
        return contRepo.salvar(ct);
    }

    public Contratacao autorizarContratacao(String contratacaoId) {
        exigirGestor();
        Contratacao ct = contRepo.buscarPorId(contratacaoId)
                .orElseThrow(() -> new RegraNegocioException("Contratação não encontrada."));
        ct.autorizar();
        return contRepo.salvar(ct);
    }

    /**
     * Efetiva uma contratação, fecha a vaga e registra o funcionário (simulação do Financeiro).
     */
    public void efetivarContratacao(String contratacaoId) {
        Contratacao ct = contRepo.buscarPorId(contratacaoId)
                .orElseThrow(() -> new RegraNegocioException("Contratação não encontrada."));
        Candidatura c = candRepo.buscarPorId(ct.getCandidaturaId())
                .orElseThrow(() -> new RegraNegocioException("Candidatura não encontrada."));
        Vaga v = vagaRepo.buscarPorId(c.getVagaId())
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));
        exigirRecrutadorDaVaga(v);

        if (ct.getDataAutorizacao() == null)
            throw new RegraNegocioException("A contratação precisa ser autorizada pelo gestor antes da efetivação.");

        // TODO: integração real com o módulo Financeiro
        // Simulação: registrar funcionário básico
        String funcionarioCpf = c.getCandidatoCpf();
        System.out.println("[SIMULAÇÃO FINANCEIRO] Cadastrando novo funcionário: CPF " + funcionarioCpf +
                " | Cargo: " + v.getCargo() + " | Salário: " + v.getSalarioBase() +
                " | Regime: " + ct.getRegime());

        ct.marcarEfetivada();
        contRepo.salvar(ct);

        v.fechar();
        vagaRepo.salvar(v);
    }

    //CONSULTAS DE APOIO / RELATÓRIOS

    public List<Candidatura> listarCandidaturasDaVaga(String vagaId) {
        return candRepo.listarPorVaga(vagaId);
    }

    public List<Entrevista> listarEntrevistasDaCandidatura(String candidaturaId) {
        return entRepo.listarPorCandidatura(candidaturaId);
    }

    /** Retorna um resumo numérico do pipeline de uma vaga (status das candidaturas). */
    public String pipelineResumo(String vagaId) {
        var cands = candRepo.listarPorVaga(vagaId);
        Map<StatusCandidatura, Long> cont = cands.stream()
                .collect(Collectors.groupingBy(Candidatura::getStatus, Collectors.counting()));
        return "Pipeline da Vaga " + vagaId + " → " + cont;
    }
}
