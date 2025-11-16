package Seguranca.dominio;

// Importes necessários para o código (verifique se os caminhos estão corretos no seu ambiente)
// Nota: Você deve ter o AdministracaoGestao no seu classpath para compilar
// Nota: Você deve ter a Candidatura.excecoes.AutorizacaoException acessível
import AdministracaoGestao.modelos.GestorMaster;
import Candidatura.excecoes.AutorizacaoException;
import java.util.ArrayList;

public class Usuario extends Pessoa {

    private long idUsuario;
    private String login;
    private String senha;
    private String tipo;

    private ArrayList<Usuario> usuarios = new ArrayList<>(); // Armazenamento interno (original)

    // --- CONSTRUTOR 1: PARA PERSISTÊNCIA CSV (COMPLETO) ---
    public Usuario (long idUsuario, String login, String senha, String tipo,
                    String nome, String cpf_cnpj, String status, String departamento) {

        // Chama o construtor de 4 argumentos de Pessoa
        super(nome, cpf_cnpj, status, departamento);
        this.idUsuario = idUsuario;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    // --- CONSTRUTOR 2: PARA CRIAÇÃO SIMPLIFICADA (USADO PELO REPOSITORY/SERVICE) ---
    public Usuario (long idUsuario, String login, String senha, String tipo)  {
        // Chama Pessoa() default
        this.idUsuario = idUsuario;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    // --- CONSTRUTOR 3: COM GESTOR MASTER (ORIGINAL) ---
    public Usuario (long idUsuario, String login, String senha, String tipo, GestorMaster gestor)  {

        if (gestor != null) {
            this.idUsuario = idUsuario;
            this.login = login;
            this.senha = senha;
            this.tipo = tipo;
        } else {
            throw new AutorizacaoException("Apenas o GestorMaster pode alterar os dados do usuário.");
        }
    }

    // --- GETTERS (ORIGINAIS) ---

//    public String getNome() {return nome;}
    public long getIdUsuario () { return this.idUsuario; }
    public String getLogin () { return this.login; }
    public String getSenha () { return this.senha; }
    public String getTipo () { return this.tipo; }

    // --- SETTERS SIMPLIFICADOS (NECESSÁRIOS PARA O REPOSITÓRIO CSV) ---
    public void setIdUsuario (long idUsuario) { this.idUsuario = idUsuario; }
    public void setLogin (String login) { this.login = login; }
    public void setSenha (String senha) { this.senha = senha; }
    public void setTipo (String tipo) { this.tipo = tipo; }


    // --- SETTERS COM GESTOR MASTER (ORIGINAIS) ---

    public void setIdUsuario (long idUsuario, GestorMaster gestor) {

        if (gestor != null) {
            this.idUsuario = idUsuario;
        } else {
            throw new AutorizacaoException("Apenas o GestorMaster pode alterar os dados do usuário.");
        }
    }

    public void setLogin (String login, GestorMaster gestor) {

        if (gestor != null) {
            this.login = login;
        } else {
            throw new AutorizacaoException("Apenas o GestorMaster pode alterar os dados do usuário.");
        }
    }

    public void setSenha (String senha, GestorMaster gestor) {

        if (gestor != null) {
            this.senha = senha;
        } else {
            throw new AutorizacaoException("Apenas o GestorMaster pode alterar os dados do usuário.");
        }
    }

    public void setTipo (String tipo, GestorMaster gestor) {

        if (gestor != null) {
            this.tipo = tipo;
        } else {
            throw new AutorizacaoException("Apenas o GestorMaster pode alterar os dados do usuário.");
        }
    }

    // --- MÉTODOS DE LÓGICA (ORIGINAIS) ---

    public boolean autenticar (String login, String senha) {

        return this.login.equals(login) && this.senha != null && this.senha.equals(senha);
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