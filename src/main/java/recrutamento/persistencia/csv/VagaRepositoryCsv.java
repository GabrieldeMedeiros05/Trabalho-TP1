package recrutamento.persistencia.csv;

import recrutamento.dominio.*;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.VagaRepository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class VagaRepositoryCsv implements VagaRepository {
    private final Path arquivo;
    private static final String CAB = "id;cargo;departamento;salarioBase;requisitos;regime;status;dataAbertura;gestorCpf;recrutadorCpf";

    public VagaRepositoryCsv(Path arquivo) {
        this.arquivo = arquivo;
        inicializar();
    }

    private void inicializar() {
        try {
            if (Files.notExists(arquivo.getParent())) Files.createDirectories(arquivo.getParent());
            if (Files.notExists(arquivo)) Files.writeString(arquivo, CAB + System.lineSeparator());
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de vagas", e);
        }
    }

    @Override
    public Vaga salvar(Vaga v) {
        List<Vaga> todos = listarTodos();
        todos.removeIf(x -> x.getId().equals(v.getId()));
        todos.add(v);
        escreverTodos(todos);
        return v;
    }

    @Override
    public Optional<Vaga> buscarPorId(String id) {
        return listarTodos().stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public List<Vaga> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines().skip(1).filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo vagas", e);
        }
    }

    @Override
    public void remover(String id) {
        List<Vaga> todos = listarTodos().stream()
                .filter(v -> !v.getId().equals(id)).collect(Collectors.toList());
        escreverTodos(todos);
    }

    @Override
    public List<Vaga> filtrar(String cargo, String departamento, StatusVaga status,
                              RegimeContratacao regime, Double faixaMin, Double faixaMax) {
        return listarTodos().stream().filter(v -> {
            boolean ok = true;
            if (cargo != null && !v.getCargo().toLowerCase().contains(cargo.toLowerCase())) ok = false;
            if (departamento != null && !v.getDepartamento().equalsIgnoreCase(departamento)) ok = false;
            if (status != null && v.getStatus() != status) ok = false;
            if (regime != null && v.getRegime() != regime) ok = false;
            if (faixaMin != null && v.getSalarioBase() < faixaMin) ok = false;
            if (faixaMax != null && v.getSalarioBase() > faixaMax) ok = false;
            return ok;
        }).collect(Collectors.toList());
    }

    private Vaga parse(String l) {
        String[] t = l.split(";", -1);
        Vaga v = new Vaga(
                t[0], t[1], t[2], Double.parseDouble(t[3]), t[4],
                RegimeContratacao.valueOf(t[5]), t[8]
        );
        if ("FECHADA".equals(t[6])) v.fechar();
        v.setRecrutadorResponsavelCpf(t[9].isBlank() ? null : t[9]);
        return v;
    }

    private void escreverTodos(List<Vaga> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB); bw.newLine();
            for (Vaga v: todos) {
                bw.write(String.join(";", List.of(
                        v.getId(), v.getCargo(), v.getDepartamento(),
                        String.valueOf(v.getSalarioBase()), v.getRequisitos(),
                        v.getRegime().name(), v.getStatus().name(),
                        v.getDataAbertura().toString(),
                        v.getGestorResponsavelCpf(),
                        v.getRecrutadorResponsavelCpf() == null ? "" : v.getRecrutadorResponsavelCpf()
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo vagas", e);
        }
    }
}
