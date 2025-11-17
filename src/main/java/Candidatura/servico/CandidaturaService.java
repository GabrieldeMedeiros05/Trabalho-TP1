package Candidatura.servico;

import Candidatura.dominio.Candidato;
import Candidatura.dominio.Candidatura;
import Candidatura.excecoes.RegraNegocioException;
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

        validarCpf(candidato.getCpf_cnpj());

        // Regra de negócio: Verificar se o candidato já existe
        if (candidatoRepository.buscarPorCpf(candidato.getCpf_cnpj()).isPresent()) {
            throw new RegraNegocioException("Candidato com este CPF já cadastrado.");
        }

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

    public void validarCpf(String cpf) {
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");

        // 1. Checagem de Tamanho
        if (cpfNumeros.length() != 11) {
            throw new RegraNegocioException("O CPF deve conter exatamente 11 dígitos.");
        }

        // 2. Checagem de Dígitos Repetidos (rápida e obrigatória)
        if (cpfNumeros.matches("(\\d)\\1{10}")) {
            throw new RegraNegocioException("CPF inválido (todos os dígitos são iguais).");
        }

        // 3. Checagem dos Dígitos Verificadores (DV)

        try {
            // Extrai os 9 primeiros dígitos
            String d0 = cpfNumeros.substring(0, 9);

            // Calcula o primeiro dígito verificador (DV1)
            int dv1 = calcularDigitoVerificador(d0, 10);


            String d1 = d0 + dv1;
            int dv2 = calcularDigitoVerificador(d1, 11);


            String dvc = String.valueOf(dv1) + String.valueOf(dv2);


            String dvOriginal = cpfNumeros.substring(9, 11);

            if (!dvc.equals(dvOriginal)) {
                throw new RegraNegocioException("CPF inválido.");
            }

        } catch (Exception e) {
            // Captura NumberFormatException ou outros erros internos
            throw new RegraNegocioException("CPF inválido.");
        }
    }
    private int calcularDigitoVerificador(String base, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        for (int i = 0; i < base.length(); i++) {
            int digito = Character.getNumericValue(base.charAt(i));
            soma += digito * peso;
            peso--;
        }

        int resto = soma % 11;

        return (resto < 2) ? 0 : (11 - resto);
    }

    public List<Candidatura> listarTodasCandidaturas() {
        return candidaturaRepository.listarTodas();
    }
}