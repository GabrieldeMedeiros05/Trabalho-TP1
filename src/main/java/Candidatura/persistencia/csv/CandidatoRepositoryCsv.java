package Candidatura.persistencia.csv;

import Candidatura.dominio.Candidato;
import Candidatura.excecoes.PersistenciaException;
import Candidatura.persistencia.CandidatoRepository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CandidatoRepositoryCsv implements CandidatoRepository {

    private final Path arquivo;
    private static final String CAB = "cpf;nome;email;telefone;linkCurriculo;status;departamento;perfilProfissional;curriculo";
    // Mínimo de campos para uma linha básica
    private static final int MIN_CAMPOS = 3;

    public CandidatoRepositoryCsv(Path arquivo) {
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
            throw new PersistenciaException("Falha ao inicializar arquivo de candidatos", e);
        }
    }

    @Override
    public Candidato salvar(Candidato candidato) {
        List<Candidato> todos = listarTodos();
        todos.removeIf(x -> x.getCpf_cnpj().equals(candidato.getCpf_cnpj()));
        todos.add(candidato);
        escreverTodos(todos);
        return candidato;
    }

    @Override
    public Optional<Candidato> buscarPorCpf(String cpf) {
        return listarTodos()
                .stream()
                .filter(c -> c.getCpf_cnpj().equals(cpf))
                .findFirst();
    }

    @Override
    public List<Candidato> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1)
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo candidatos", e);
        }
    }

    @Override
    public boolean deletarPorCpf(String cpf) {
        List<Candidato> todos = listarTodos();
        boolean foiRemovido = todos.removeIf(c -> c.getCpf_cnpj().equals(cpf));
        if (foiRemovido) {
            escreverTodos(todos);
        }
        return foiRemovido;
    }

    private Candidato parse(String l) {
        try {
            String[] t = l.split(";", -1);

            if (t.length < MIN_CAMPOS) {
                System.err.println("Linha incompleta no CSV: " + l);
                return null;
            }

            // Atribuição de variáveis seguindo o CAB:
            // 0:cpf, 1:nome, 2:email, 3:telefone, 4:linkCurriculo, 5:status, 6:departamento, 7:perfil, 8:curriculo

            // Campos Pessoa
            String cpf = t[0];
            String nome = t[1];
            String status = t.length > 5 ? t[5] : "";
            String departamento = t.length > 6 ? t[6] : "";

            // Campos Candidato
            String email = t[2];
            String telefone = t.length > 3 ? t[3] : "";
            String linkCurriculo = t.length > 4 ? t[4] : "";
            String perfilProfissional = t.length > 7 ? t[7] : "";
            String curriculo = t.length > 8 ? t[8] : "";

            Candidato c = new Candidato(nome, cpf, status, departamento,
                    email, telefone, linkCurriculo,
                    perfilProfissional, curriculo);

            return c;
        } catch (Exception e) {
            System.err.println("Erro processando linha: " + l + ". Detalhe: " + e.getMessage());
            return null;
        }
    }

    private void escreverTodos(List<Candidato> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo)) {
            bw.write(CAB);
            bw.newLine();
            for (Candidato c : todos) {
                bw.write(String.join(";", List.of(
                        c.getCpf_cnpj(),
                        c.getNome(),
                        c.getEmail(),
                        c.getTelefone() != null ? c.getTelefone() : "",
                        c.getLinkCurriculo() != null ? c.getLinkCurriculo() : "",
                        c.getStatus() != null ? c.getStatus() : "",
                        c.getPerfilProfissional() != null ? c.getPerfilProfissional() : "",
                        c.getCurriculo() != null ? c.getCurriculo() : ""
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo candidatos", e);
        }
    }
}