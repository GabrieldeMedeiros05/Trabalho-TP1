package Seguranca.dominio;

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


    public Funcionario(
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
        super(nome, fisicaOuJuridica, cpf_cnpj, dataNascimento, emailPessoal, telefonePessoal, enderecoCompleto);
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

}