package recrutamento.persistencia.csv;

import recrutamento.dominio.*;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.CandidaturaRepository;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CandidaturaRepositoryCsv implements CandidaturaRepository {
    private final Path arquivo;
    private static final String CAB = "id;vagaId;candidatoCpf;status;dataCriacao";

    public CandidaturaRepositoryCsv(Path arquivo){
        this.arquivo = arquivo;
        inicializar();
    }
    private void inicializar(){
        try {
            if (Files.notExists(arquivo.getParent())) Files.createDirectories(arquivo.getParent());
            if (Files.notExists(arquivo)) Files.writeString(arquivo, CAB + System.lineSeparator());
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de candidaturas", e);
        }
    }

    @Override
    public Candidatura salvar(Candidatura c) {
        List<Candidatura> todos = listarTodas();
        todos.removeIf(x -> x.getId().equals(c.getId()));
        todos.add(c);
        escreverTodos(todos);
        return c;
    }

    @Override
    public Optional<Candidatura> buscarPorId(String id) {
        return listarTodas().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<Candidatura> listarPorVaga(String vagaId) {
        return listarTodas().stream().filter(c -> c.getVagaId().equals(vagaId)).collect(Collectors.toList());
    }

    @Override
    public List<Candidatura> listarTodas() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines().skip(1).filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo candidaturas", e);
        }
    }

    private Candidatura parse(String l) {
        String[] t = l.split(";", -1);
        Candidatura c = new Candidatura(t[0], t[1], t[2]);
        // status
        try {
            var field = Candidatura.class.getDeclaredField("status");
            field.setAccessible(true);
            field.set(c, StatusCandidatura.valueOf(t[3]));
        } catch (Exception ignore) { }
        // dataCriacao
        try {
            var f = Candidatura.class.getDeclaredField("dataCriacao");
            f.setAccessible(true);
            f.set(c, LocalDateTime.parse(t[4]));
        } catch (Exception ignore) { }
        return c;
    }

    private void escreverTodos(List<Candidatura> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB); bw.newLine();
            for (Candidatura c: todos) {
                bw.write(String.join(";", List.of(
                        c.getId(), c.getVagaId(), c.getCandidatoCpf(),
                        c.getStatus().name(), c.getDataCriacao().toString()
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo candidaturas", e);
        }
    }
}
