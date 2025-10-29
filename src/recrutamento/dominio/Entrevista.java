package recrutamento.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de entrevista de uma candidatura.
 * Integrações futuras:
 *  - Agenda/Notificações (se houver)
 */
public class Entrevista {
    private final String id;              // UUID
    private final String candidaturaId;   // referência forte (string)
    private final LocalDateTime dataHora;
    private final String avaliador;       // pode ser CPF ou nome (dep. do módulo Administração)
    private Double nota;                  // opcional
    private String parecer;               // opcional

    public Entrevista(String id, String candidaturaId, LocalDateTime dataHora, String avaliador) {
        this.id = Objects.requireNonNull(id);
        this.candidaturaId = Objects.requireNonNull(candidaturaId);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.avaliador = Objects.requireNonNull(avaliador);
    }

    public void registrarAvaliacao(Double nota, String parecer) {
        this.nota = nota;
        this.parecer = parecer;
    }

    public String getId() { return id; }
    public String getCandidaturaId() { return candidaturaId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getAvaliador() { return avaliador; }
    public Double getNota() { return nota; }
    public String getParecer() { return parecer; }
}
