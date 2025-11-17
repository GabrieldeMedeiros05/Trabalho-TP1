package recrutamento.dominio;

/**
 * Representa o usuário do tipo Recrutador.
 * Integrações futuras:
 *  - Administração/Gestão: herdar de Usuario (quando disponível)
 */
public class Recrutador {
    private final String cpf; // por enquanto o identificador principal

    public Recrutador(String cpf) { this.cpf = cpf; }
    public String getCpf() { return cpf; }
}
