package Seguranca.servico;

import Candidatura.excecoes.AutorizacaoException;
import Candidatura.excecoes.RegraNegocioException;
import Seguranca.persistencia.UsuarioRepository;
import Seguranca.dominio.Usuario; // Classe de domínio do módulo de Segurança


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
        if (!usuario.autenticar(login, senha)) { // <--- A lógica CRÍTICA
            throw new AutorizacaoException("Senha incorreta.");
        }


        return usuario;
    }

    /**
     * Cria um novo registro de Usuário associado a um Candidato.
     * @param login Login desejado.
     * @param senha Senha.
     * @param cpfCandidato CPF do candidato (para link com dados pessoais).
     * @return O Usuario salvo.
     * @throws RegraNegocioException Se o login já estiver em uso.
     */
    public Usuario criarUsuarioCandidato(String login, String senha, String cpfCandidato) {
        // Regra de Negócio: Login deve ser único
        if (repository.buscarPorLogin(login).isPresent()) {
            throw new RegraNegocioException("Login '" + login + "' já em uso.");
        }

        // Cria o Usuário (ID 0L para ser gerado na persistência; Tipo fixo: CANDIDATO)
        Usuario novo = new Usuario(0L, login, senha, "CANDIDATO");

        return repository.salvar(novo);
    }

    /**
     * Cadastra um novo usuário interno (RH/Admin), verificando unicidade de Login e CPF.
     * @param usuario O objeto Usuario a ser salvo.
     * @return O Usuario salvo com ID gerado.
     * @throws RegraNegocioException Se o login ou CPF/CNPJ já estiverem em uso.
     */
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Regra de Negócio 1: Login deve ser único
        if (repository.buscarPorLogin(usuario.getLogin()).isPresent()) {
            throw new RegraNegocioException("O login '" + usuario.getLogin() + "' já está em uso.");
        }

        // Regra de Negócio 2: CPF/CNPJ deve ser único
        if (usuario.getCpf_cnpj() != null && repository.buscarPorCpfPessoa(usuario.getCpf_cnpj()).isPresent()) {
            throw new RegraNegocioException("O CPF/CNPJ '" + usuario.getCpf_cnpj() + "' já está cadastrado.");
        }

        return repository.salvar(usuario);
    }

    public Optional<Usuario> validarCpf(String cpf, String nome) {

        Optional<Usuario> usuario = repository.buscarPorCpfPessoa(cpf);

        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuario não existe");
        }

        if (!usuario.get().getNome().equals(nome)) {
            throw new RegraNegocioException("Nome do usuário incompatível com o CPF");
        }

        return usuario;

    }

    public void editarUsuario(String nome, String login, String senha, String tipo, Optional<Usuario> usuario) {

        repository.editar(nome, login, senha, tipo, usuario);
    }

    public void excluirUsuario(String cpf, String nome) {

//        validarCpf(cpf, nome);

        Optional<Usuario> usuario = repository.buscarPorCpfPessoa(cpf);

        if (usuario.isEmpty()) {
            throw new RegraNegocioException("Usuario não existe");
        }

        if (!usuario.get().getNome().equals(nome)) {
            throw new RegraNegocioException("Nome do usuário incompatível com o CPF");
        }

        repository.excluir(cpf);
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = repository.listarTodos();
//        return repository.listarTodos();

        return lista;
    }
}
