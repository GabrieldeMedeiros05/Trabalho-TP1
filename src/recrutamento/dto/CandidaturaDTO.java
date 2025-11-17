package recrutamento.dto;

public class CandidaturaDTO {

    private String id;
    private String vagaId;
    private String candidatoCpf;
    private String status; // APROVADO, REPROVADO, EM_ANALISE, etc.

    public CandidaturaDTO(String id,
                          String vagaId,
                          String candidatoCpf,
                          String status) {
        this.id = id;
        this.vagaId = vagaId;
        this.candidatoCpf = candidatoCpf;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getVagaId() {
        return vagaId;
    }

    public String getCandidatoCpf() {
        return candidatoCpf;
    }

    public String getStatus() {
        return status;
    }
}
