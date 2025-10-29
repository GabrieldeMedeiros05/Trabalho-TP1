package pessoas;

import java.util.ArrayList;

public class Pessoa {
    
    private String nome;
    private String cpf_cnpj;
    private String status;
    private String departamento;

    private ArrayList<Pessoa> pessoas;

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
