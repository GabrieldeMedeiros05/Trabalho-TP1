package Seguranca.persistencia.csv;

import Candidatura.excecoes.PersistenciaException;
import Seguranca.persistencia.UsuarioRepository;
import Seguranca.dominio.Usuario; // Seu domínio isolado

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistência CSV simples para Usuário (data/usuarios.csv).
 */
public class UsuarioRepositoryCsv implements UsuarioRepository {

    private final Path arquivo;
    private static final String CAB = "idUsuario;login;senha;tipo;nome;cpf_cnpj;status;departamento";
    // Número mínimo de campos esperados (idUsuario a tipo)
    private static final int MIN_CAMPOS_NECESSARIOS = 4;

    public UsuarioRepositoryCsv(Path arquivo) {
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

                // CRIAÇÃO DE USUÁRIOS DE TESTE (COMPLETOS)
                Usuario uTeste = new Usuario(
                        1L, "candidato", "123", "CANDIDATO",
                        "Candidato Teste", "12345678900", "ATIVO", "CANDIDATURA"
                );
                salvar(uTeste);

                Usuario rhTeste = new Usuario(
                        2L, "rh", "456", "RH",
                        "Recrutador Teste", "99999999900", "ATIVO", "RECRUTAMENTO"
                );
                salvar(rhTeste);
            }
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de usuários", e);
        }
    }

    @Override
    public Usuario salvar(Usuario u) {
        List<Usuario> todos = listarTodos();

        todos.removeIf(x -> x.getIdUsuario() == u.getIdUsuario());

        if (u.getIdUsuario() == 0) {
            long novoId = todos.stream().mapToLong(Usuario::getIdUsuario).max().orElse(0) + 1;
            u.setIdUsuario(novoId);
        }

        todos.add(u);
        escreverTodos(todos);
        return u;
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return listarTodos()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorCpfPessoa(String cpf) {
        return listarTodos()
                .stream()
                .filter(u -> u.getNumeroRegistro() != null && u.getNumeroRegistro().equals(cpf))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1)
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo usuários", e);
        }
    }


    private Usuario parse(String l) {
        try {
            String[] t = l.split(";", -1);

            if (t.length < MIN_CAMPOS_NECESSARIOS) {
                System.err.println("Linha incompleta no CSV de usuários: " + l);
                return null;
            }

            // Atributos de Usuario
            long idUsuario = Long.parseLong(t[0]);
            String login = t[1];
            String senha = t[2];
            String tipo = t[3];

            // Atributos de Pessoa (Acessados com verificação de índice)
            String nome = t.length > 4 ? t[4] : "";
            String cpfCnpj = t.length > 5 ? t[5] : "";
            String status = t.length > 6 ? t[6] : "";
            String departamento = t.length > 7 ? t[7] : ""; // Se o array tem 8+ elementos (índice 7)

            // Cria o usuário usando o construtor completo
            return new Usuario(
                    idUsuario,
                    login,
                    senha,
                    tipo,
                    nome,
                    cpfCnpj,
                    status,
                    departamento
            );

        } catch (Exception e) {
            System.err.println("Erro processando linha: " + l + ". Detalhe: " + e.getMessage());
            return null;
        }
    }

    private void escreverTodos(List<Usuario> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            bw.write(CAB);
            bw.newLine();
            for (Usuario u : todos) {
                bw.write(String.join(";", List.of(
                        String.valueOf(u.getIdUsuario()),
                        u.getLogin(),
                        u.getSenha(),
                        u.getTipo(),
                        // Dados de Pessoa (com tratamento de nulo)
                        u.getNome() != null ? u.getNome() : "",
                        u.getNumeroRegistro() != null ? u.getNumeroRegistro() : "",
                        u.getStatus() != null ? u.getStatus() : "",
                        u.getDepartamento() != null ? u.getDepartamento() : ""
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo usuários", e);
        }
    }
}