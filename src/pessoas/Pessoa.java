package pessoas;

import java.util.ArrayList;


public class Pessoa {

    private String nome;
    private String cpf_cnpj;
    private String status;
    private String departamento;

    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    // Adicionar um construtor
    public Pessoa() {}

    public Pessoa (String nome, String cpf_cnpj, String status, String departamento) {
        
        this.nome = nome;
        this.cpf_cnpj = cpf_cnpj;
        this.status = status;
        this.departamento = departamento;
    }

    // Adicionar setters (pois os atributos são privados)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
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


    public Pessoa pesquisar (String nome) {

        for (Pessoa pessoa : pessoas) {
            if (nome.equals(pessoa.nome)) {

                return pessoa;
            }
        }

        return null; 
    }

    // ... restante da classe
}