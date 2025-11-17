package Seguranca.servico;

import Candidatura.excecoes.AutorizacaoException;
import Candidatura.excecoes.RegraNegocioException;
import Seguranca.dominio.Usuario;
import Seguranca.persistencia.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    /**
     * Tenta autenticar um usuário com login e senha.
     * @param login O login fornecido.
     * @param senha A senha fornecida.
     * @return O objeto Usuario se a autenticação for bem-sucedida.
     * @throws AutorizacaoException Se o login ou senha estiverem incorretos/vazios.
     */
    public Usuario autenticar(String login, String senha) {
        if (login == null || login.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new AutorizacaoException("Login ou senha não podem ser vazios.");
        }

        Optional<Usuario> optUsuario = repository.buscarPorLogin(login);

        if (optUsuario.isEmpty()) {
            throw new AutorizacaoException("Usuário não encontrado.");
        }

        Usuario usuario = optUsuario.get();
        if (!usuario.autenticar(login, senha)) {
            throw new AutorizacaoException("Senha incorreta.");
        }

        return usuario;
    }

    /**
     * Cria um novo registro de Usuário associado a um Candidato.
     * Tipo fixo: "CANDIDATO".
     */
    public Usuario criarUsuarioCandidato(String login, String senha, String cpfCandidato) {
        if (repository.buscarPorLogin(login).isPresent()) {
            throw new RegraNegocioException("Login '" + login + "' já em uso.");
        }

        Usuario novo = new Usuario(0L, login, senha, "CANDIDATO");
        return repository.salvar(novo);
    }

    /**
     * Cadastra um novo usuário interno (RH/Admin), verificando unicidade de Login e CPF/CNPJ.
     */
    public Usuario cadastrarUsuario(Usuario usuario) {
        if (repository.buscarPorLogin(usuario.getLogin()).isPresent()) {
            throw new RegraNegocioException("O login '" + usuario.getLogin() + "' já está em uso.");
        }

        if (usuario.getCpf_cnpj() != null
                && repository.buscarPorCpfPessoa(usuario.getCpf_cnpj()).isPresent()) {
            throw new RegraNegocioException("O CPF/CNPJ '" + usuario.getCpf_cnpj() + "' já está cadastrado.");
        }

        return repository.salvar(usuario);
    }

    /**
     * Valida se existe usuário com o CPF e se o nome bate.
     */
    public Optional<Usuario> validarCpf(String cpf, String nome) {
        Optional<Usuario> usuario = repository.buscarPorCpfPessoa(cpf);

        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuário não existe");
        }

        if (!usuario.get().getNome().equals(nome)) {
            throw new RegraNegocioException("Nome do usuário incompatível com o CPF");
        }

        return usuario;
    }

    /**
     * Edita um usuário existente.
     */
    public void editarUsuario(String nome,
                              String login,
                              String senha,
                              String tipo,
                              Optional<Usuario> usuario) {
        repository.editar(nome, login, senha, tipo, usuario);
    }

    /**
     * Exclui um usuário pelo CPF, validando nome.
     */
    public void excluirUsuario(String cpf, String nome) {
        Optional<Usuario> usuario = repository.buscarPorCpfPessoa(cpf);

        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuário não existe");
        }

        if (!usuario.get().getNome().equals(nome)) {
            throw new RegraNegocioException("Nome do usuário incompatível com o CPF");
        }

        repository.excluir(cpf);
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    public List<Usuario> listarTodos() {
        return repository.listarTodos();
    }

    /**
     * Lista usuários por tipo (ex.: "RECRUTADOR", "GESTOR", "CANDIDATO").
     */
    public List<Usuario> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new RegraNegocioException("Tipo de usuário não pode ser vazio.");
        }

        return repository.listarTodos().stream()
                .filter(u -> u.getTipo() != null && u.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas usuários do tipo RECRUTADOR.
     * Usado pelo módulo de Recrutamento para atribuir vagas.
     */
    public List<Usuario> listarRecrutadores() {
        return listarPorTipo("RECRUTADOR");
    }
}
