package recrutamento.dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Processo de contratação decorrente de uma candidatura aprovada.
 * Integrações futuras:
 *  - Administração/Gestão: autorização por Gestor
 *  - Financeiro: criação do Funcionário
 */
public class Contratacao {
    private final String id;                 // UUID
    private final String candidaturaId;
    private final RegimeContratacao regime;
    private final LocalDateTime dataSolicitacao = LocalDateTime.now();

    private LocalDateTime dataAutorizacao;  // quando Gestor autoriza (integração futura)
    private boolean efetivada;

    public Contratacao(String id, String candidaturaId, RegimeContratacao regime) {
        this.id = Objects.requireNonNull(id);
        this.candidaturaId = Objects.requireNonNull(candidaturaId);
        this.regime = Objects.requireNonNull(regime);
    }

    public void autorizar() { this.dataAutorizacao = LocalDateTime.now(); }
    public void marcarEfetivada() { this.efetivada = true; }

    public boolean isEfetivada() { return efetivada; }
    public LocalDateTime getDataAutorizacao() { return dataAutorizacao; }
    public String getId() { return id; }
    public String getCandidaturaId() { return candidaturaId; }
    public RegimeContratacao getRegime() { return regime; }
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
}