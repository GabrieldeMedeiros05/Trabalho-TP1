package Seguranca.dominio;

// Importes necessários para o código (verifique se os caminhos estão corretos no seu ambiente)
// Nota: Você deve ter o AdministracaoGestao no seu classpath para compilar
// Nota: Você deve ter a Candidatura.excecoes.AutorizacaoException acessível

public class Usuario extends Pessoa {

    private long idUsuario;
    private String login;
    private String senha;
    private String tipo;


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

    // --- MÉTODOS DE LÓGICA (ORIGINAIS) ---

    public boolean autenticar (String login, String senha) {

        return this.login.equals(login) && this.senha != null && this.senha.equals(senha);
    }

}