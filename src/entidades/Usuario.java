package entidades;

import java.util.ArrayList;

import AdministracaoGestao.gestao.Gestor;

public class Usuario extends Pessoa {
    
    private long idUsuario;
    private String login;
    private String senha;
    private String tipo;

    private ArrayList<Usuario> usuarios;

    public Usuario (long idUsuario, String login, String senha, String tipo, Gestor gestor)  {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.idUsuario = idUsuario;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    } else {
        throw new SecurityException("Apenas gestores podem alterar os dados do usuário.");
    }
        
        
    }

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
