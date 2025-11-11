package Candidatura.servico;

import Candidatura.dominio.Candidato;
import Candidatura.dominio.Candidatura;
import Candidatura.persistencia.CandidatoRepository;
import Candidatura.persistencia.CandidaturaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;
    private final CandidatoRepository candidatoRepository;

    public CandidaturaService(
            CandidaturaRepository candidaturaRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.candidaturaRepository = candidaturaRepository;
        this.candidatoRepository = candidatoRepository;
    }

    // --- Métodos de Candidato ---

    public void salvarCandidato(Candidato candidato) {
        candidatoRepository.salvar(candidato);
    }

    public List<Candidato> listarCandidatos() {
        return candidatoRepository.listarTodos();
    }

    public Optional<Candidato> buscarCandidatoPorCpf(String cpf) {
        return candidatoRepository.buscarPorCpf(cpf);
    }

    public boolean deletarCandidato(String cpf) {
        // Regra de Negócio: Não deve ser possível deletar um candidato com candidaturas ativas
        List<Candidatura> candidaturas = listarCandidaturasPorCpf(cpf);

        if (!candidaturas.isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir o candidato. Há " + candidaturas.size() + " candidaturas registradas.");
        }

        return candidatoRepository.deletarPorCpf(cpf);
    }

    // --- Métodos de Candidatura ---

    public Candidatura registrarCandidatura(String vagaId, String candidatoCpf) {
        if (buscarCandidatoPorCpf(candidatoCpf).isEmpty()) {
            throw new IllegalArgumentException("Candidato com CPF " + candidatoCpf + " não encontrado.");
        }
        String novoId = UUID.randomUUID().toString();
        Candidatura nova = new Candidatura(novoId, vagaId, candidatoCpf);
        return candidaturaRepository.salvar(nova);
    }

    public List<Candidatura> listarCandidaturasPorCpf(String cpf) {
        return candidaturaRepository.listarTodas()
                .stream()
                .filter(c -> c.getCandidatoCpf().equals(cpf))
                .collect(Collectors.toList());
    }

    public List<Candidatura> listarTodasCandidaturas() {
        return candidaturaRepository.listarTodas();
    }
}