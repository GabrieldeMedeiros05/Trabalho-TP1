package recrutamento.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de entrevista de uma candidatura.
 * Integrações futuras:
 *  - Agenda/Notificações (se houver)
 */
public class Entrevista {

    private final String id;            // UUID
    private final String candidaturaId; // referência forte (string)

    private LocalDateTime dataHora;
    private String avaliador;           // pode ser CPF ou nome (dep. do módulo Administração)
    private Double nota;                // opcional
    private String parecer;             // opcional

    /**
     * Construtor básico, usado normalmente pelo RecrutamentoService ao agendar entrevista.
     */
    public Entrevista(String id,
                      String candidaturaId,
                      LocalDateTime dataHora,
                      String avaliador) {
        this.id = Objects.requireNonNull(id);
        this.candidaturaId = Objects.requireNonNull(candidaturaId);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.avaliador = Objects.requireNonNull(avaliador);
    }

    /**
     * Construtor completo (útil para o repositório CSV, se quiser carregar nota/parecer).
     */
    public Entrevista(String id,
                      String candidaturaId,
                      LocalDateTime dataHora,
                      String avaliador,
                      Double nota,
                      String parecer) {
        this(id, candidaturaId, dataHora, avaliador);
        this.nota = nota;
        this.parecer = parecer;
    }

    /**
     * Atalho para registrar nota e parecer em conjunto.
     */
    public void registrarAvaliacao(Double nota, String parecer) {
        this.nota = nota;
        this.parecer = parecer;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getCandidaturaId() {
        return candidaturaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getAvaliador() {
        return avaliador;
    }

    public Double getNota() {
        return nota;
    }

    public String getParecer() {
        return parecer;
    }

    // Setters (para edição)

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = Objects.requireNonNull(dataHora);
    }

    public void setAvaliador(String avaliador) {
        this.avaliador = Objects.requireNonNull(avaliador);
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }
}
