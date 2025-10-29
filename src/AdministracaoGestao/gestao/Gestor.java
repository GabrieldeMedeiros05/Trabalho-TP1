package AdministracaoGestao.gestao;

import entidades.Pessoa;
import entidades.Usuario;


public class Gestor extends Pessoa {
    
    private String loginMaster;
    private String senhaMaster;

    Gestor (String nome, String cpf, String status, String departamento, String loginMaster, String senhaMaster) {

        super(nome,cpf,status,departamento);

        this.loginMaster = loginMaster;

        this.senhaMaster = senhaMaster;

    }  

    public Usuario cadastrar (long idUsuario, String login, String senha, String tipo) {

        Usuario user = new Usuario(idUsuario, login, senha, tipo, this); 

        return user;
    } 

}
