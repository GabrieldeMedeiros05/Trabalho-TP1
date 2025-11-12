package Seguranca.dominio;

public class Funcionario extends Pessoa {
    private String matricula;
    private String cargo;
    private String departamento;
    private String dataContratacao;
    private float salarioBase;
    private String status;
    private String tipoContrato;
    private int cargaHoraria;
    private String emailCorporativo;
    private String telefoneFixo;
    private String telefoneResidencial;
    private String telefoneCelular;


    public Funcionario(
            String nome,
            String personalidade,
            String numeroRegistro,
            String dataNascimento,
            String matricula,
            String cargo,
            String departamento,
            String dataContratacao,
            float salarioBase,
            String status,
            String tipoContrato,
            int cargaHoraria,
            String emailCorporativo,
            String emailPessoal,
            String telefoneFixo,
            String telefoneResidencial,
            String telefoneCelular,
            String telefonePessoal,
            String enderecoCompleto
    ) {
        super(nome, personalidade, numeroRegistro, dataNascimento, emailPessoal, telefonePessoal, enderecoCompleto);
        this.matricula = matricula;
        this.cargo = cargo;
        this.departamento = departamento;
        this.dataContratacao = dataContratacao;
        this.salarioBase = salarioBase;
        this.status = status;
        this.tipoContrato = tipoContrato;
        this.cargaHoraria = cargaHoraria;
        this.emailCorporativo = emailCorporativo;
        this.telefoneFixo = telefoneFixo;
        this.telefoneResidencial = telefoneResidencial;
        this.telefoneCelular = telefoneCelular;
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



    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

}
