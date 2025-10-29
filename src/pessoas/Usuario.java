package pessoas;

import java.util.ArrayList;

public class Usuario extends Pessoa {
    
    private long idUsuario;
    private String login;
    private String senha;
    private String tipo;

    private ArrayList<Usuario> usuarios;

    public boolean autenticar (String login, String senha) {

        if (this.login.equals(login) && this.senha.equals(senha)) {
            
            return true;
        }

        return false;
    }

    public Usuario pesquisarUsuario (String status) {

        for (Usuario usuario : usuarios) {
            if (status.equals(usuario.getStatus())) {

                return usuario; 
            }
        }

        return null; 
    }

}
