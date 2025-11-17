package Seguranca.dominio;

import utils.Constantes;

public class Funcionario extends Pessoa {
    private long idPessoa;
    private String matricula;
    private String cargo;
    private String departamento;
    private String dataContratacao;
    private float salarioBase;
    private String status;
    private String tipoContrato;
    private int cargaHoraria;
    private String emailCorporativo;
    private String telefoneRamal;
    private String telefoneCelularCorporativo;
    private boolean valeTransporte;
    private boolean valeAlimentacao;
    private boolean isentoImpostos;
    private float imposto = 0f;


    public Funcionario(
            long idPessoa,
            String nome,
            String fisicaOuJuridica,
            String cpf_cnpj,
            String dataNascimento,
            String emailPessoal,
            String telefonePessoal,
            String enderecoCompleto,
            String matricula,
            String cargo,
            String departamento,
            String dataContratacao,
            float salarioBase,
            String status,
            String tipoContrato,
            int cargaHoraria,
            String emailCorporativo,
            String telefoneRamal,
            String telefoneCelularCorporativo
    ) {
        super(idPessoa, nome, fisicaOuJuridica, cpf_cnpj, dataNascimento, emailPessoal, telefonePessoal, enderecoCompleto);
        this.matricula = matricula;
        this.cargo = cargo;
        this.departamento = departamento;
        this.dataContratacao = dataContratacao;
        this.salarioBase = salarioBase;
        this.status = status;
        this.tipoContrato = tipoContrato;
        this.cargaHoraria = cargaHoraria;
        this.emailCorporativo = emailCorporativo;
        this.telefoneRamal = telefoneRamal;
        this.telefoneCelularCorporativo = telefoneCelularCorporativo;
    }

    public Funcionario(
        long idPessoa,
        String matricula,
        String cargo,
        String departamento,
        String dataContratacao,
        float salarioBase,
        String status,
        String tipoContrato,
        int cargaHoraria,
        String emailCorporativo,
        String telefoneRamal,
        String telefoneCelularCorporativo,
        boolean valeTransporte,
        boolean valeAlimentacao,
        boolean isentoImpostos
    ) {
        this.idPessoa = idPessoa;
        this.matricula = matricula;
        this.cargo = cargo;
        this.departamento = departamento;
        this.dataContratacao = dataContratacao;
        this.salarioBase = salarioBase;
        this.status = status;
        this.tipoContrato = tipoContrato;
        this.cargaHoraria = cargaHoraria;
        this.emailCorporativo = emailCorporativo;
        this.telefoneRamal = telefoneRamal;
        this.telefoneCelularCorporativo = telefoneCelularCorporativo;
    }

    public String getEmailCorporativo() {
        return emailCorporativo;
    }

    public void setEmailCorporativo(String emailCorporativo) {
        this.emailCorporativo = emailCorporativo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(String dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public float getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public String getStatus() {
        if (this.status.equalsIgnoreCase("pj")) {
            return "false";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getTelefoneRamal() {
        return telefoneRamal;
    }

    public void setTelefoneRamal(String telefoneRamal) {
        this.telefoneRamal = telefoneRamal;
    }

    public String getTelefoneCelularCorporativo() {
        return telefoneCelularCorporativo;
    }

    public void setTelefoneCelularCorporativo(String telefoneCelularCorporativo) {
        this.telefoneCelularCorporativo = telefoneCelularCorporativo;
    }

    public long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public boolean isValeTransporte() {
        return valeTransporte;
    }

    public void setValeTransporte(boolean valeTransporte) {
        this.valeTransporte = valeTransporte;
    }

    public boolean isValeAlimentacao() {
        return valeAlimentacao;
    }

    public void setValeAlimentacao(boolean valeAlimentacao) {
        this.valeAlimentacao = valeAlimentacao;
    }

    public boolean isIsentoImpostos() {
        return isentoImpostos;
    }

    public void setIsentoImpostos(boolean isentoImpostos) {
        this.isentoImpostos = isentoImpostos;
    }

    public String getValeTransporteSn() {
        if (valeTransporte) {return "Sim";}
        return "Nao";
    }
    public String getValeAlimentacaoSn() {
        if (valeAlimentacao) {return "Sim";}
        return "Nao";
    }
    public String getIsentoImpostosSn() {
        if (isentoImpostos) {return "Sim";}
        return "Nao";
    }

    public float calculaSalarioLiquido() {
        float salarioLiquido = getSalarioBase();
        if (getStatus().equalsIgnoreCase("pj")) {
            return salarioLiquido;
        }

        if ((getTipoContrato().equalsIgnoreCase("clt") || getTipoContrato().equalsIgnoreCase("temporário")) && !isIsentoImpostos() && salarioBase > 2000f) {
            imposto = salarioLiquido*0.1f;
            salarioLiquido *= 0.9f;
        }

        if (isValeTransporte()) {
            salarioLiquido += Constantes.VALOR_VALE_TRANSPORTE;
        }
        if (isValeAlimentacao()) {
            salarioLiquido += Constantes.VALOR_VALE_ALIMENTACAO;
        }

        return salarioLiquido;
    }

    public float getImposto() {
        return imposto;
    }

    @Override
    public String toString() {
        return idPessoa+", "+matricula+", "+getNome()+", "+cargo+", "+departamento+", "+dataContratacao+", "+salarioBase+", "+status+", "+tipoContrato+", "+cargaHoraria+", "+emailCorporativo+", "+telefoneRamal+", "+telefoneCelularCorporativo;
    }

}