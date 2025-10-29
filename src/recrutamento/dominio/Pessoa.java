package recrutamento.dominio;

public class Pessoa {

    private String nome;
    private String cpf_cnpj;
    // ... outros atributos

    // Adicionar um construtor
    public Pessoa() {}

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

    // ... restante da classe
}