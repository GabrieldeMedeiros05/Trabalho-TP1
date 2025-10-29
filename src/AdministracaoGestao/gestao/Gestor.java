package AdministracaoGestao.gestao;

import java.util.ArrayList;

import entidades.Pessoa;
import entidades.Usuario;
import pessoas.Usuario;


public class Gestor extends Pessoa {
    
    private String loginMaster;
    private String senhaMaster;
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    Gestor (String nome, String cpf, String status, String departamento, String loginMaster, String senhaMaster) {

        super(nome,cpf,status,departamento);

        this.loginMaster = loginMaster;

        this.senhaMaster = senhaMaster;

    }  

    public Usuario cadastrar (long idUsuario, String login, String senha, String tipo) {

        Usuario user = new Usuario(idUsuario, login, senha, tipo, this); 

        usuarios.add(user);

        return user;
    }

    public void editar (long idOriginal, long idUsuario, String login, String senha, String tipo) {

        boolean usuarioEditado = false;
        
        for (Usuario user : usuarios) {
            
            if (user.getIdUsuario() == idOriginal) {
                
                user.setIdUsuario(idUsuario, this);
                user.setLogin(login, this);
                user.setSenha(senha, this);
                user.setTipo(tipo, this);

                usuarioEditado = true;
                break;
            }
        }
        
        
       if (usuarioRemovido == false) {
           
            throw new Exception("Usuário não encontrado.");
        }
    }

    public void excluir (Usuario usuario) {

        boolean usuarioRemovido = false;

        for (Usuario user : usuarios) {
            
            if (user.getIdUsuario() == usuario.getIdUsuario()) {
                
                usuarios.remove(user);   
                usuarioRemovido = true;
                break;
            }
        }

        if (usuarioRemovido == false) {
           
            throw new Exception("Usuário não encontrado.");
        }
    }

    public ArrayList<Long> listar () {

        ArrayList<Long> IDs = new ArrayList<>();

        for (Usuario user : usuarios) {
            
            IDs.add(user.getIdUsuario());
        }

        return IDs;
    }

    public boolean validar (Usuario usuario) {

        boolean usuarioEncontrado = false;

        for (Usuario user : usuarios) {
            
            if (user.getIdUsuario() == usuario.getIdUsuario()) {
                  
                usuarioEncontrado = true;
                break;
            }
        }

        if (usuarioEncontrado == false) {
           
            throw new Exception("Usuário não encontrado.");
            return false;
        }

        return usuarioEncontrado;
    }  

    public ArrayList<String> gerarRelatorio () {

        ArrayList<String> relatos = new ArrayList<>();

        for (Usuario user : usuarios) {
            
            relatos.add(String.format("ID: %s Login: %s Senha: %s Tipo: %s", user.getIdUsuario(), user.getLogin(), user.getSenha(), user.getTipo()));
        }

        return relatos;
    }

}
