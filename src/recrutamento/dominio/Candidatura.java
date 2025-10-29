package recrutamento.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Vincula um Candidato a uma Vaga.
 * Integrações futuras:
 *  - Candidatura/Candidato: consulta dados completos do candidato
 */
public class Candidatura {
    private final String id;           // UUID
    private final String vagaId;       // referência forte (string)
    private final String candidatoCpf; // referência leve (CPF)
    private StatusCandidatura status = StatusCandidatura.PENDENTE;
    private final LocalDateTime dataCriacao = LocalDateTime.now();

    public Candidatura(String id, String vagaId, String candidatoCpf) {
        this.id = Objects.requireNonNull(id);
        this.vagaId = Objects.requireNonNull(vagaId);
        this.candidatoCpf = Objects.requireNonNull(candidatoCpf);
    }

    // Restringe mutação do status apenas via serviço (amarrar regra)
    void setStatus(StatusCandidatura novo) { this.status = Objects.requireNonNull(novo); }

    public String getId() { return id; }
    public String getVagaId() { return vagaId; }
    public String getCandidatoCpf() { return candidatoCpf; }
    public StatusCandidatura getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
