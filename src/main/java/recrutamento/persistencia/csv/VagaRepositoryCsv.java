package recrutamento.persistencia.csv;

import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;
import recrutamento.excecoes.PersistenciaException;
import recrutamento.persistencia.VagaRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VagaRepositoryCsv implements VagaRepository {

    private final Path arquivo;

    // id;codigo;cargo;departamento;salarioBase;requisitos;regime;status;dataAbertura;gestorCpf;recrutadorCpf
    private static final String CAB =
            "id;codigo;cargo;departamento;salarioBase;requisitos;regime;status;dataAbertura;gestorCpf;recrutadorCpf";

    public VagaRepositoryCsv(Path arquivo) {
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
        return listarTodos().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Vaga> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1)
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo vagas", e);
        }
    }

    @Override
    public void remover(String id) {
        List<Vaga> todos = listarTodos().stream()
                .filter(v -> !v.getId().equals(id))
                .collect(Collectors.toList());
        escreverTodos(todos);
    }

    @Override
    public List<Vaga> filtrar(String cargo,
                              String departamento,
                              StatusVaga status,
                              RegimeContratacao regime,
                              Double faixaMin,
                              Double faixaMax) {

        return listarTodos().stream()
                .filter(v -> {
                    boolean ok = true;
                    if (cargo != null && !v.getCargo().toLowerCase().contains(cargo.toLowerCase())) ok = false;
                    if (departamento != null && !v.getDepartamento().equalsIgnoreCase(departamento)) ok = false;
                    if (status != null && v.getStatus() != status) ok = false;
                    if (regime != null && v.getRegime() != regime) ok = false;
                    if (faixaMin != null && v.getSalarioBase() < faixaMin) ok = false;
                    if (faixaMax != null && v.getSalarioBase() > faixaMax) ok = false;
                    return ok;
                })
                .collect(Collectors.toList());
    }

    private Vaga parse(String l) {
        String[] t = l.split(";", -1);

        // Esperado:
        // 0:id
        // 1:codigo
        // 2:cargo
        // 3:departamento
        // 4:salarioBase
        // 5:requisitos
        // 6:regime
        // 7:status
        // 8:dataAbertura
        // 9:gestorCpf
        //10:recrutadorCpf

        Vaga v = new Vaga(
                t[0],                         // id
                t[2],                         // cargo
                t[3],                         // departamento
                Double.parseDouble(t[4]),     // salarioBase
                t[5],                         // requisitos
                RegimeContratacao.valueOf(t[6]), // regime
                t[9],                         // gestorResponsavelCpf
                t[1]                          // codigo (6 dígitos)
        );

        if ("FECHADA".equalsIgnoreCase(t[7])) {
            v.fechar();
        }

        if (t.length > 10 && !t[10].isBlank()) {
            v.setRecrutadorResponsavelCpf(t[10]);
        }

        // Observação: dataAbertura está sendo lida do CSV em t[8],
        // mas a classe Vaga atual define dataAbertura = LocalDate.now()
        // e não possui setter para data. Se quiser recuperar a data real
        // do CSV, será necessário ajustar a modelagem da Vaga.
        return v;
    }

    private void escreverTodos(List<Vaga> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB);
            bw.newLine();

            for (Vaga v : todos) {
                String linha = String.join(";", List.of(
                        v.getId(),
                        v.getCodigo() == null ? "" : v.getCodigo(),
                        v.getCargo(),
                        v.getDepartamento(),
                        String.valueOf(v.getSalarioBase()),
                        v.getRequisitos() == null ? "" : v.getRequisitos(),
                        v.getRegime().name(),
                        v.getStatus().name(),
                        v.getDataAbertura().toString(),
                        v.getGestorResponsavelCpf(),
                        v.getRecrutadorResponsavelCpf() == null ? "" : v.getRecrutadorResponsavelCpf()
                ));
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo vagas", e);
        }
    }
}
