package Candidatura.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Vincula um Candidato a uma Vaga.
 */
public class Candidatura {
    private final String id;
    private final String vagaId;
    private final String candidatoCpf;
    private StatusCandidatura status = StatusCandidatura.PENDENTE;
    private final LocalDateTime dataCriacao = LocalDateTime.now();

    public Candidatura(String id, String vagaId, String candidatoCpf) {
        this.id = Objects.requireNonNull(id);
        this.vagaId = Objects.requireNonNull(vagaId);
        this.candidatoCpf = Objects.requireNonNull(candidatoCpf);
    }

    public void setStatus(StatusCandidatura novo) { // TORNADO PÚBLICO para gestão de status
        this.status = Objects.requireNonNull(novo);
    }

    public String getId() { return id; }
    public String getVagaId() { return vagaId; }
    public String getCandidatoCpf() { return candidatoCpf; }
    public StatusCandidatura getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        return id + " | vaga=" + vagaId + " | cpf=" + candidatoCpf + " | status=" + status + " | criado=" + dataCriacao;
    }
}