package entidades;

import java.util.ArrayList;

import AdministracaoGestao.gestao.Gestor;

public class Pessoa {
    
    private String nome;
    private String cpf_cnpj;
    private String status;
    private String departamento;

    private ArrayList<Pessoa> pessoas;

    public Pessoa () {

    }

    public Pessoa (String nome, String cpf_cnpj, String status, String departamento) {
        
        this.nome = nome;
        this.cpf_cnpj = cpf_cnpj;
        this.status = status;
        this.departamento = departamento;
    }

    public String getStatus () {

        return this.status;
    }

    public Pessoa pesquisar (String nome) {

        for (Pessoa pessoa : pessoas) {
            if (nome.equals(pessoa.nome)) {

                return pessoa;
            }
        }

        return null; 
    }

}
