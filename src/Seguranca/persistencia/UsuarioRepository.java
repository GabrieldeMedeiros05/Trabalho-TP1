package Seguranca.persistencia;

import Seguranca.dominio.Usuario; // NOVO IMPORT

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    void editar(String nome, String login, String senha, String tipo, Optional<Usuario> usuario);
    void excluir(String cpf);
    Optional<Usuario> buscarPorLogin(String login);
    Optional<Usuario> buscarPorCpfPessoa(String cpf);
    List<Usuario> listarTodos();
}
