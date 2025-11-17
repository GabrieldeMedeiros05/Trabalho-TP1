package Candidatura.persistencia.csv;

import Candidatura.dominio.Candidatura;
import Candidatura.dominio.StatusCandidatura;
import Candidatura.excecoes.PersistenciaException;
import Candidatura.persistencia.CandidaturaRepository;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistência CSV simples para Candidatura.
 * Arquivo: data/candidaturas.csv
 * Cabeçalho: id;vagaId;candidatoCpf;status;dataCriacao
 */
public class CandidaturaRepositoryCsv implements CandidaturaRepository {

    private final Path arquivo;
    private static final String CAB = "id;vagaId;candidatoCpf;status;dataCriacao";

    public CandidaturaRepositoryCsv(Path arquivo) {
        this.arquivo = arquivo;
        inicializar();
    }

    /**
     * Garante que o diretório e o arquivo CSV existam.
     */
    private void inicializar() {
        try {
            if (Files.notExists(arquivo.getParent())) {
                Files.createDirectories(arquivo.getParent());
            }
            if (Files.notExists(arquivo)) {
                Files.writeString(arquivo, CAB + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de candidaturas", e);
        }
    }

    /**
     * Salva (insere ou atualiza) uma candidatura no CSV.
     */
    @Override
    public Candidatura salvar(Candidatura c) {
        List<Candidatura> todos = listarTodas();
        // Remove caso já exista uma candidatura com o mesmo ID
        todos.removeIf(x -> x.getId().equals(c.getId()));
        todos.add(c);
        escreverTodos(todos);
        return c;
    }

    /**
     * Busca uma candidatura pelo ID.
     */
    @Override
    public Optional<Candidatura> buscarPorId(String id) {
        return listarTodas()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * Lista todas as candidaturas de uma vaga específica.
     */
    @Override
    public List<Candidatura> listarPorVaga(String vagaId) {
        return listarTodas()
                .stream()
                .filter(c -> c.getVagaId().equals(vagaId))
                .collect(Collectors.toList());
    }
    @Override
    public boolean deletarPorCpf(String cpf) {

        List<Candidatura> todos = new ArrayList<>(listarTodos());

        boolean foiRemovido = todos.removeIf(c -> c.getCandidatoCpf().equals(cpf));
        if (foiRemovido) {


        }
        return foiRemovido;
    }

    private List<Candidatura> listarTodos() {
        return List.of();
    }

    /**
     * Lista todas as candidaturas salvas no CSV.
     */
    @Override
    public List<Candidatura> listarTodas() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1) // pula cabeçalho
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo candidaturas", e);
        }
    }

    /**
     * Converte uma linha do CSV em um objeto Candidatura.
     */
    private Candidatura parse(String l) {
        try {
            String[] t = l.split(";", -1);
            // id;vagaId;candidatoCpf;status;dataCriacao
            Candidatura c = new Candidatura(t[0], t[1], t[2]);
            // status
            try {
                var field = Candidatura.class.getDeclaredField("status");
                field.setAccessible(true);
                field.set(c, StatusCandidatura.valueOf(t[3]));
            } catch (Exception ignore) { /* ignore */ }
            // dataCriacao
            try {
                var f = Candidatura.class.getDeclaredField("dataCriacao");
                f.setAccessible(true);
                f.set(c, LocalDateTime.parse(t[4]));
            } catch (Exception ignore) { /* ignore */ }
            return c;
        } catch (Exception e) {
            System.err.println("Linha inválida no CSV de candidaturas: " + l);
            return null;
        }
    }

    /**
     * Regrava todas as candidaturas no arquivo.
     */
    private void escreverTodos(List<Candidatura> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB);
            bw.newLine();
            for (Candidatura c : todos) {
                bw.write(String.join(";", List.of(
                        c.getId(),
                        c.getVagaId(),
                        c.getCandidatoCpf(),
                        c.getStatus().name(),
                        c.getDataCriacao().toString()
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo candidaturas", e);
        }
    }
}