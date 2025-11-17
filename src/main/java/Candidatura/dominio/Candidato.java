package Candidatura.dominio;

import Seguranca.dominio.Pessoa; // <-- CORREÇÃO: Importa de Seguranca.dominio

public class Candidato extends Pessoa {

    private String email;
    private String telefone;
    private String linkCurriculo;
    private String perfilProfissional;
    private String curriculo;

    // CONSTRUTOR DE CRIAÇÃO
    public Candidato(String cpf, String nome, String email) {
        super(nome, cpf, "ATIVO", "CANDIDATURA");
        this.email = email;
    }

    // CONSTRUTOR COMPLETO
    public Candidato(String nome, String cpf_cnpj, String status, String departamento,
                     String email, String telefone, String linkCurriculo,
                     String perfilProfissional, String curriculo) {

        super(nome, cpf_cnpj, status, departamento);
        this.email = email;
        this.telefone = telefone;
        this.linkCurriculo = linkCurriculo;
        this.perfilProfissional = perfilProfissional;
        this.curriculo = curriculo;
    }

    // O método é herdado publicamente de Pessoa.

    // --- GETTERS E SETTERS ESPECÍFICOS ---

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getLinkCurriculo() { return linkCurriculo; }
    public void setLinkCurriculo(String linkCurriculo) { this.linkCurriculo = linkCurriculo; }

    public String getPerfilProfissional() { return perfilProfissional; }
    public void setPerfilProfissional(String perfilProfissional) { this.perfilProfissional = perfilProfissional; }

    public String getCurriculo() { return curriculo; }
    public void setCurriculo(String curriculo) { this.curriculo = curriculo; }

    @Override
    public String toString() {
        // Agora getCpf_cnpj() e getNome() são resolvidos corretamente via herança.
        return getCpf_cnpj() + " | " + getNome() + " | " + email;
    }
}