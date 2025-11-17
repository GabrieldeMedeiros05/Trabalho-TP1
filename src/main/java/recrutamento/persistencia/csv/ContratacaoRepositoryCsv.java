package recrutamento.persistencia.csv;

import recrutamento.dominio.Contratacao;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.ContratacaoRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContratacaoRepositoryCsv implements ContratacaoRepository {
    private final Path arquivo;
    private static final String CAB = "id;candidaturaId;regime;dataSolicitacao;dataAutorizacao;efetivada";

    public ContratacaoRepositoryCsv(Path arquivo){
        this.arquivo = arquivo;
        inicializar();
    }
    private void inicializar(){
        try {
            if (Files.notExists(arquivo.getParent())) Files.createDirectories(arquivo.getParent());
            if (Files.notExists(arquivo)) Files.writeString(arquivo, CAB + System.lineSeparator());
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de contratações", e);
        }
    }

    @Override
    public Contratacao salvar(Contratacao c) {
        List<Contratacao> todos = listarTodas();
        todos.removeIf(x -> x.getId().equals(c.getId()));
        todos.add(c);
        escreverTodos(todos);
        return c;
    }

    @Override
    public Optional<Contratacao> buscarPorId(String id) {
        return listarTodas().stream().filter(ct -> ct.getId().equals(id)).findFirst();
    }

    @Override
    public List<Contratacao> listarTodas() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines().skip(1).filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo contratações", e);
        }
    }

    private Contratacao parse(String l) {
        String[] t = l.split(";", -1);
        var c = new Contratacao(t[0], t[1], recrutamento.dominio.RegimeContratacao.valueOf(t[2]));
        // datas e flags por reflexão (campos privados sem setters públicos)
        try {
            var f = Contratacao.class.getDeclaredField("dataAutorizacao");
            f.setAccessible(true);
            f.set(c, t[4].isBlank() ? null : LocalDateTime.parse(t[4]));
        } catch (Exception ignore){}
        try {
            var f2 = Contratacao.class.getDeclaredField("efetivada");
            f2.setAccessible(true);
            f2.setBoolean(c, Boolean.parseBoolean(t[5]));
        } catch (Exception ignore){}
        return c;
    }

    private void escreverTodos(List<Contratacao> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB); bw.newLine();
            for (Contratacao c: todos) {
                bw.write(String.join(";", List.of(
                        c.getId(), c.getCandidaturaId(), c.getRegime().name(),
                        c.getDataSolicitacao().toString(),
                        c.getDataAutorizacao() == null ? "" : c.getDataAutorizacao().toString(),
                        String.valueOf(c.isEfetivada())
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo contratações", e);
        }
    }
}
