package Seguranca.dominio;

import java.util.ArrayList;


public class Pessoa {

    private long id;
    private String nome;
    private String personalidade;
    private String numeroRegistro;
    private String dataNascimento;
    private String emailPessoal;
    private String telefonePessoal;
    private String enderecoCompleto;

    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    // Adicionar um construtor
    public Pessoa() {}

    public Pessoa (long id, String nome, String personalidade, String numeroRegistro, String dataNascimento, String emailPessoal, String telefonePessoal, String enderecoCompleto) {
        this.id = id;
        this.nome = nome;
        this.personalidade = personalidade;
        this.numeroRegistro = numeroRegistro;
        this.dataNascimento = dataNascimento;
        this.emailPessoal = emailPessoal;
        this.telefonePessoal = telefonePessoal;
        this.enderecoCompleto = enderecoCompleto;
    }

    public Pessoa (String nome, String personalidade, String numeroRegistro) {
        this.nome = nome;
        this.personalidade = personalidade;
        this.numeroRegistro = numeroRegistro;
    }

    // Adicionar setters (pois os atributos são privados)
    public void setId(long id) {this.id = id;}
    public void setNome(String nome) {this.nome = nome;}
    public void setPersonalidade(String personalidade) {this.personalidade = personalidade;}
    public void setDataNascimento(String dataNascimento) {this.dataNascimento = dataNascimento;}
    public void setEmailPessoal(String emailPessoal) {this.emailPessoal = emailPessoal;}
    public void setTelefonePessoal(String telefonePessoal) {this.telefonePessoal = telefonePessoal;}
    public void setEnderecoCompleto(String enderecoCompleto) {this.enderecoCompleto = enderecoCompleto;}
    // Adicionar getter
    public long getId() {return id;}
    public String getNome() {return nome;}
    public String getNumeroRegistro() {return numeroRegistro;}
    public String getDataNascimento() {return dataNascimento;}
    public String getEmailPessoal() {return emailPessoal;}
    public String getTelefonePessoal() {return telefonePessoal;}
    public String getEnderecoCompleto() {return enderecoCompleto;}


}