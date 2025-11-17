package recrutamento.dominio;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa uma Vaga no sistema de RH.
 * Integrações futuras:
 *  - Administração/Gestão: validação de perfis (Gestor/Recrutador)
 *  - Candidatura: vínculo com candidaturas (por id da vaga)
 *  - Financeiro: fechamento ao efetivar contratação
 */
public class Vaga {
    private final String id;
    private String codigo;// UUID (chave forte)
    private String cargo;
    private String departamento;
    private double salarioBase;
    private String requisitos;
    private RegimeContratacao regime;
    private StatusVaga status = StatusVaga.ABERTA;
    private final LocalDate dataAbertura = LocalDate.now();

    // Integrações externas por enquanto como CPFs (strings) para evitar acoplamento:
    private final String gestorResponsavelCpf;       // TODO: integrar com módulo Administração/Gestão
    private String recrutadorResponsavelCpf;         // TODO: integrar com módulo Administração/Gestão

    public Vaga(String id, String cargo, String departamento, double salarioBase,
                String requisitos, RegimeContratacao regime, String gestorResponsavelCpf, String codigo) {
        this.id = Objects.requireNonNull(id);
        this.cargo = Objects.requireNonNull(cargo);
        this.departamento = Objects.requireNonNull(departamento);
        this.salarioBase = salarioBase;
        this.requisitos = requisitos;
        this.regime = Objects.requireNonNull(regime);
        this.gestorResponsavelCpf = Objects.requireNonNull(gestorResponsavelCpf);
        this.codigo = codigo;
    }

    // Regras simples
    public void fechar() { this.status = StatusVaga.FECHADA; }
    public boolean estaAberta() { return this.status == StatusVaga.ABERTA; }

    // Getters/Setters essenciais (encapsulamento)
    public String getId() { return id; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }
    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }
    public RegimeContratacao getRegime() { return regime; }
    public void setRegime(RegimeContratacao regime) { this.regime = regime; }
    public StatusVaga getStatus() { return status; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public String getGestorResponsavelCpf() { return gestorResponsavelCpf; }
    public String getRecrutadorResponsavelCpf() { return recrutadorResponsavelCpf; }
    public void setRecrutadorResponsavelCpf(String recrutadorResponsavelCpf) {
        this.recrutadorResponsavelCpf = recrutadorResponsavelCpf;
    }

    public String getCodigo() { return codigo; }
}
