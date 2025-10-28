package recrutamento.persistencia.csv;

import recrutamento.dominio.Entrevista;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.EntrevistaRepository;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EntrevistaRepositoryCsv implements EntrevistaRepository {
    private final Path arquivo;
    private static final String CAB = "id;candidaturaId;dataHora;avaliador;nota;parecer";

    public EntrevistaRepositoryCsv(Path arquivo){
        this.arquivo = arquivo;
        inicializar();
    }
    private void inicializar(){
        try {
            if (Files.notExists(arquivo.getParent())) Files.createDirectories(arquivo.getParent());
            if (Files.notExists(arquivo)) Files.writeString(arquivo, CAB + System.lineSeparator());
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de entrevistas", e);
        }
    }

    @Override
    public Entrevista salvar(Entrevista e) {
        List<Entrevista> todos = listarPorCandidatura(null); // truque pra carregar todos
        todos.removeIf(x -> x.getId().equals(e.getId()));
        todos.add(e);
        escreverTodos(todos);
        return e;
    }

    @Override
    public List<Entrevista> listarPorCandidatura(String candidaturaId) {
        List<Entrevista> lista;
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            lista = br.lines().skip(1).filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo entrevistas", e);
        }
        if (candidaturaId == null) return lista;
        return lista.stream().filter(i -> i.getCandidaturaId().equals(candidaturaId)).collect(Collectors.toList());
    }

    private Entrevista parse(String l) {
        String[] t = l.split(";", -1);
        Entrevista e = new Entrevista(t[0], t[1], LocalDateTime.parse(t[2]), t[3]);
        if (!t[4].isBlank() || !t[5].isBlank()) {
            try {
                var m = Entrevista.class.getMethod("registrarAvaliacao", Double.class, String.class);
                Double nota = t[4].isBlank() ? null : Double.valueOf(t[4]);
                m.invoke(e, nota, t[5].isBlank() ? null : t[5]);
            } catch (Exception ignore) {}
        }
        return e;
    }

    private void escreverTodos(List<Entrevista> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB); bw.newLine();
            for (Entrevista e: todos) {
                bw.write(String.join(";", List.of(
                        e.getId(), e.getCandidaturaId(), e.getDataHora().toString(),
                        e.getAvaliador(),
                        e.getNota() == null ? "" : String.valueOf(e.getNota()),
                        e.getParecer() == null ? "" : e.getParecer()
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo entrevistas", e);
        }
    }
}
