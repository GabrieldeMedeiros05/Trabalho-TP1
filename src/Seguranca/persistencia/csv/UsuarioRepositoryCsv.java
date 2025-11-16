package Seguranca.persistencia.csv;

import Candidatura.excecoes.PersistenciaException;
import Candidatura.excecoes.RegraNegocioException;
import Seguranca.persistencia.UsuarioRepository;
import Seguranca.dominio.Usuario; // Seu domínio isolado

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


import java.io.FileWriter;
import java.io.FileReader;
import java.nio.file.Files;
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
    public Usuario salvar(Usuario usuario) {
        List<Usuario> todos = listarTodos();

        todos.removeIf(x -> x.getIdUsuario() == usuario.getIdUsuario());

        if (usuario.getIdUsuario() == 0) {
            long novoId = todos.stream().mapToLong(Usuario::getIdUsuario).max().orElse(0) + 1;
            usuario.setIdUsuario(novoId);
        }

        todos.add(usuario);
        escreverTodos(todos);
        return usuario;
    }

    @Override
    public void editar(String nome, String login, String senha, String tipo, Optional<Usuario> usuario) {

        ArrayList<String> usuarios = new ArrayList<>();

        File diretorio = new File("./");
        String caminho =  diretorio.getAbsolutePath();
        caminho = caminho.substring(0, caminho.length() - 1);
        caminho = caminho + "data/usuarios.csv";


        File arquivoUsuarios = new File(caminho);

        try (Scanner myReader = new Scanner(arquivoUsuarios)) {
            while (myReader.hasNextLine()) {
                usuarios.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        arquivoUsuarios.delete();

        try {
            FileWriter myWriter = new FileWriter(caminho);
            for (String elem : usuarios) {

                String usuarioNovo = elem;

                if (elem.contains(usuario.get().getCpf_cnpj())) {

                    if (!nome.isEmpty() & !login.isEmpty() & !senha.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!login.isEmpty() & !senha.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!nome.isEmpty() & !senha.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!nome.isEmpty() & !login.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    if (!nome.isEmpty() & !login.isEmpty() & !senha.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    if (!nome.isEmpty() & !login.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getLogin(), login);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!nome.isEmpty() & !senha.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!nome.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!login.isEmpty() & senha.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getSenha(), senha);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!login.isEmpty() & tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getLogin(), login);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!senha.isEmpty() & !tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getSenha(), senha);
                        usuarioNovo = usuarioNovo.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!nome.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getNome(), nome);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!login.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getLogin(), login);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!senha.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getSenha(), senha);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                    else if (!tipo.isEmpty()) {
                        usuarioNovo = elem.replace(usuario.get().getTipo(), tipo);
                        myWriter.write(usuarioNovo);
                        myWriter.write("\n");
                        continue;
                    }
                }
                
                myWriter.write(usuarioNovo);
                myWriter.write("\n");
            }

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(String cpf) {

        ArrayList<String> usuarios = new ArrayList<>();

        File diretorio = new File("./");
        String caminho =  diretorio.getAbsolutePath();
        caminho = caminho.substring(0, caminho.length() - 1);
        caminho = caminho + "data/usuarios.csv";


        File arquivoUsuarios = new File(caminho);

        try (Scanner myReader = new Scanner(arquivoUsuarios)) {
            while (myReader.hasNextLine()) {
                usuarios.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        arquivoUsuarios.delete();

        try {
            FileWriter myWriter = new FileWriter(caminho);
            for (String elem : usuarios) {

                if (elem.contains(cpf)) {
                    continue;
                }

                myWriter.write(elem);
                myWriter.write("\n");
            }

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                .filter(u -> u.getCpf_cnpj() != null && u.getCpf_cnpj().equals(cpf))
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
                        u.getCpf_cnpj() != null ? u.getCpf_cnpj() : "",
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
