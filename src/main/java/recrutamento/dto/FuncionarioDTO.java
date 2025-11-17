package recrutamento.dto;

import recrutamento.dominio.RegimeContratacao;

public class FuncionarioDTO {

    private String cpf;
    private String cargo;
    private double salarioBase;
    private RegimeContratacao regime;

    public FuncionarioDTO(String cpf,
                          String cargo,
                          double salarioBase,
                          RegimeContratacao regime) {
        this.cpf = cpf;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
        this.regime = regime;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public RegimeContratacao getRegime() {
        return regime;
    }
}
