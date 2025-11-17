package recrutamento.persistencia.csv;

import recrutamento.dominio.Entrevista;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.EntrevistaRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntrevistaRepositoryCsv implements EntrevistaRepository {

    private final Path arquivo;
    private static final String CAB = "id;candidaturaId;dataHora;avaliador;nota;parecer";

    public EntrevistaRepositoryCsv(Path arquivo) {
        this.arquivo = arquivo;
        inicializar();
    }

    private void inicializar() {
        try {
            if (Files.notExists(arquivo.getParent())) {
                Files.createDirectories(arquivo.getParent());
            }
            if (Files.notExists(arquivo)) {
                Files.writeString(arquivo, CAB + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de entrevistas", e);
        }
    }

    @Override
    public Entrevista salvar(Entrevista e) {
        List<Entrevista> todos = listarTodos();
        todos.removeIf(x -> x.getId().equals(e.getId()));
        todos.add(e);
        escreverTodos(todos);
        return e;
    }

    @Override
    public Optional<Entrevista> buscarPorId(String id) {
        return listarTodos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Entrevista> listarPorCandidatura(String candidaturaId) {
        return listarTodos().stream()
                .filter(e -> Objects.equals(e.getCandidaturaId(), candidaturaId))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(String id) {
        List<Entrevista> todos = listarTodos().stream()
                .filter(e -> !e.getId().equals(id))
                .collect(Collectors.toList());
        escreverTodos(todos);
    }

    // ===================== AUXILIARES =====================

    private List<Entrevista> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1)                // pula cabeçalho
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo entrevistas", e);
        }
    }

    private Entrevista parse(String l) {
        String[] t = l.split(";", -1); // -1 para manter campos vazios

        // t[0] = id
        // t[1] = candidaturaId
        // t[2] = dataHora
        // t[3] = avaliador
        // t[4] = nota (opcional)
        // t[5] = parecer (opcional)

        Entrevista e = new Entrevista(
                t[0],
                t[1],
                LocalDateTime.parse(t[2]),
                t[3]
        );

        Double nota = null;
        if (!t[4].isBlank()) {
            nota = Double.valueOf(t[4]);
        }
        String parecer = t[5].isBlank() ? null : t[5];

        if (nota != null || parecer != null) {
            e.registrarAvaliacao(nota, parecer);
        }

        return e;
    }

    private void escreverTodos(List<Entrevista> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB);
            bw.newLine();
            for (Entrevista e : todos) {
                bw.write(String.join(";", List.of(
                        e.getId(),
                        e.getCandidaturaId(),
                        e.getDataHora().toString(),
                        e.getAvaliador(),
                        e.getNota() == null ? "" : String.valueOf(e.getNota()),
                        e.getParecer() == null ? "" : e.getParecer()
                )));
                bw.newLine();
            }
        } catch (IOException ex) {
            throw new PersistenciaException("Erro escrevendo entrevistas", ex);
        }
    }
}
