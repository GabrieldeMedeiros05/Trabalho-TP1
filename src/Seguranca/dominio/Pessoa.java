package Seguranca.dominio;

import java.util.ArrayList;


public class Pessoa {
    public void setStatus(String status) {
        this.status = status;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getFisicaOuJuridica() {
        return fisicaOuJuridica;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getEmailPessoal() {
        return emailPessoal;
    }

    public String getTelefonePessoal() {
        return telefonePessoal;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    private String nome;
    private String cpf_cnpj;
    private String status;
    private String departamento;
    // Editado para conter informações relacionadas a pessoa
    private long id;
    private String fisicaOuJuridica;
    private String dataNascimento;
    private String emailPessoal;
    private String telefonePessoal;
    private String enderecoCompleto;

    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    // Adicionar um construtor
    public Pessoa() {}

    public Pessoa (String nome, String cpf_cnpj, String status, String departamento) {

        this.nome = nome;
        this.cpf_cnpj = cpf_cnpj;
        this.status = status;
        this.departamento = departamento;
    }

    public Pessoa (long id, String nome, String fisicaOuJuridica, String cpf_cnpj, String dataNascimento, String emailPessoal, String telefonePessoal, String enderecoCompleto) {
        this.id = id;
        this.nome = nome;
        this.cpf_cnpj = cpf_cnpj;
        this.fisicaOuJuridica = fisicaOuJuridica;
        this.dataNascimento = dataNascimento;
        this.emailPessoal = emailPessoal;
        this.telefonePessoal = telefonePessoal;
        this.enderecoCompleto = enderecoCompleto;
    }

    // Adicionar setters (pois os atributos são privados)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFisicaOuJuridica(String fisicaOuJuridica) {
        this.fisicaOuJuridica = fisicaOuJuridica;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEmailPessoal(String emailPessoal) {
        this.emailPessoal = emailPessoal;
    }

    public void setTelefonePessoal(String telefonePessoal) {
        this.telefonePessoal = telefonePessoal;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    // Adicionar getter para nome
    public String getNome() {
        return nome;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public String getStatus () {

        return this.status;
    }

    // Em Seguranca.dominio.Pessoa.java

    // Adicionar getter para departamento (CRÍTICO PARA O REPOSITÓRIO CSV)
    public String getDepartamento () { // Deve ser público
        return this.departamento;
    }

// ... (resto dos getters e setters)


    public Pessoa pesquisar (String nome) {

        for (Pessoa pessoa : pessoas) {
            if (nome.equals(pessoa.nome)) {

                return pessoa;
            }
        }

        return null;
    }

    @Override
    public String toString () {
        return id+", "+nome+", "+fisicaOuJuridica+", "+cpf_cnpj+", "+dataNascimento+", "+emailPessoal+", "+telefonePessoal+", "+enderecoCompleto;

    }

    public long getId() {
        return id;
    }

    // ... restante da classe
}