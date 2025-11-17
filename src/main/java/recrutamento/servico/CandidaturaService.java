package recrutamento.servico;

import recrutamento.dominio.*;
import recrutamento.excecoes.*;
import recrutamento.persistencia.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio do módulo de Candidaturas.
 *
 * Permite:
 * - Registrar novas candidaturas (recrutador);
 * - Atualizar status das candidaturas;
 * - Listar candidaturas por vaga ou candidato;
 * - Gerar resumo do pipeline de uma vaga.
 *
 * Aplica os princípios de POO:
 * - Abstração (interfaces de repositório);
 * - Encapsulamento (atributos privados e acesso controlado);
 * - Polimorfismo (diferentes ações conforme o perfil);
 * - Herança (planejada para Recrutador → Usuario).
 */
public class CandidaturaService {

    private final CandidaturaRepository candRepo;
    private final VagaRepository vagaRepo;
    private final EntrevistaRepository entRepo;

    // Simulação de sessão (até integração com módulo Administração)
    private String usuarioCpf;
    private String perfil; // "GESTOR" ou "RECRUTADOR"

    public CandidaturaService(CandidaturaRepository candRepo,
                              VagaRepository vagaRepo,
                              EntrevistaRepository entRepo) {
        this.candRepo = candRepo;
        this.vagaRepo = vagaRepo;
        this.entRepo = entRepo;
    }

    // CONTROLE SIMPLIFICADO DE SESSÃO
    public void logarComoGestor(String cpf) {
        this.usuarioCpf = cpf;
        this.perfil = "GESTOR";
    }

    public void logarComoRecrutador(String cpf) {
        this.usuarioCpf = cpf;
        this.perfil = "RECRUTADOR";
    }

    // Nota: A classe Vaga não foi fornecida, mas assumimos que tem getRecrutadorResponsavelCpf() e estaAberta().
    private void exigirRecrutadorDaVaga(Vaga v) {
        if (!"RECRUTADOR".equals(perfil) ||
                !Objects.equals(v.getRecrutadorResponsavelCpf(), usuarioCpf)) {
            throw new AutorizacaoException("Acesso negado: recrutador não responsável por esta vaga.");
        }
    }

    // ======================
    // CANDIDATURAS
    // ======================

    public Candidatura registrarCandidatura(String vagaId, String candidatoCpf) {
        Vaga v = vagaRepo.buscarPorId(vagaId)
                .orElseThrow(() -> new RegraNegocioException("Vaga não encontrada."));

        if (!v.estaAberta())
            throw new RegraNegocioException("Vaga não está aberta para candidaturas.");

        // A regra de negócio exige que apenas o recrutador responsável possa registrar
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

        // Uso de Reflection para contornar o encapsulamento do atributo 'status',
        // que é necessário para a persistência e o Service, mas que é um padrão
        // mais complexo. O método setStatus é de pacote (package-private) no domínio.
        try {
            var f = Candidatura.class.getDeclaredMethod("setStatus", StatusCandidatura.class);
            f.setAccessible(true);
            f.invoke(c, novo);
        } catch (Exception e) {
            // Em um ambiente de produção, seria melhor registrar o erro e retornar uma mensagem mais clara.
            throw new RegraNegocioException("Falha interna ao alterar status da candidatura.");
        }

        return candRepo.salvar(c);
    }

    public List<Candidatura> listarCandidaturasDaVaga(String vagaId) {
        return candRepo.listarPorVaga(vagaId);
    }

    /**
     * Corrige o erro de "cannot find symbol" trocando listarTodos() por listarTodas(),
     * conforme definido na interface CandidaturaRepository.
     */
    public List<Candidatura> listarCandidaturasDoCandidato(String candidatoCpf) {
        // CORREÇÃO: trocando listarTodos() por listarTodas()
        return candRepo.listarTodas().stream()
                .filter(c -> c.getCandidatoCpf().equals(candidatoCpf))
                .collect(Collectors.toList());
    }


    public List<Entrevista> listarEntrevistasDaCandidatura(String candidaturaId) {
        return entRepo.listarPorCandidatura(candidaturaId);
    }

    public String pipelineResumo(String vagaId) {
        var cands = candRepo.listarPorVaga(vagaId);
        Map<StatusCandidatura, Long> cont = cands.stream()
                .collect(Collectors.groupingBy(Candidatura::getStatus, Collectors.counting()));
        return "Pipeline da Vaga " + vagaId + " → " + cont;
    }
}