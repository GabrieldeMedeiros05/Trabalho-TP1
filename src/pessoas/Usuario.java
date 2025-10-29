package pessoas;

import AdministracaoGestao.gestao.Gestor;
import java.util.ArrayList;
import recrutamento.excecoes.AutorizacaoException;

public class Usuario extends Pessoa {
    
    private long idUsuario;
    private String login;
    private String senha;
    private String tipo;

    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario (long idUsuario, String login, String senha, String tipo, Gestor gestor)  {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.idUsuario = idUsuario;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
        } else {
            throw new AutorizacaoException("Apenas gestores podem alterar os dados do usuário.");
        } 
    }

    public long getIdUsuario () {

        return this.idUsuario;
    }

    public String getLogin () {

        return this.login;
    }

    public String getSenha () {

        return this.senha;
    }

    public String getTipo () {
        
        return this.tipo;
    } 

    public void setIdUsuario (long idUsuario, Gestor gestor) {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.idUsuario = idUsuario;
        } else {
            throw new AutorizacaoException("Apenas gestores podem alterar os dados do usuário.");
        }
    }

    public void setLogin (String login, Gestor gestor) {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.login = login;
        } else {
            throw new AutorizacaoException("Apenas gestores podem alterar os dados do usuário.");
        }
    }

    public void setSenha (String senha, Gestor gestor) {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.senha = senha;
        } else {
            throw new AutorizacaoException("Apenas gestores podem alterar os dados do usuário.");
        }
    }

    public void setTipo (String tipo, Gestor gestor) {

        if (gestor != null) { // ou uma verificação mais complexa
        
        this.tipo = tipo;
        } else {
            throw new AutorizacaoException("Apenas gestores podem alterar os dados do usuário.");
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