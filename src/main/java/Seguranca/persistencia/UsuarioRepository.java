package Seguranca.persistencia;

import Seguranca.dominio.Usuario; // NOVO IMPORT

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorLogin(String login);
    Optional<Usuario> buscarPorCpfPessoa(String cpf);
    List<Usuario> listarTodos();
}