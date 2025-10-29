package recrutamento.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma Pessoa que se candidata a uma Vaga.
 * Herda informações básicas (nome, cpf, etc.) da classe Pessoa.
 */
public class Candidato extends Pessoa {

  private String email;
     private String telefone;
   private String linkCurriculo;

    /**
     * Construtor que inicializa o Candidato com informações básicas.
     * Assume-se que o CPF/CNPJ (atributo herdado) é o identificador chave.
     * @param nome O nome completo do candidato.
     * @param cpf O CPF do candidato.
     */
    public Candidato(String nome, String cpf) {

        super.setNome(Objects.requireNonNull(nome));
        super.setCpf_cnpj(Objects.requireNonNull(cpf));
    }
    //exemplo
    @Override
    public String toString() {
        return "Candidato{" +
                "cpf='" + getCpf_cnpj() + '\'' +
                ", nome='" + getNome() + '\'' +
                '}';
    }
}