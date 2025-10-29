package Financeiro;

import java.util.ArrayList;

import pessoas.Pessoa;

public class Financeiro {
    Vinculo vinculo;
    double salarioBase;
    double salarioCalculado;
    boolean temValeTransporte;
    boolean temValeAlimentacao;
    boolean temIsencaoIr;
    double adicionalSalario;



    public static void main(String[] args) {
        new FinanceiroTelaInicial();
        new CadastroRegra();
        new VisualizaRegra();
    }

    public void calculaSalario() {
        if (this.temValeTransporte) {
            this.salarioCalculado += 300;
        }
        if (this.temValeAlimentacao) {
            this.salarioCalculado += 300;
        }
        this.salarioCalculado += (this.salarioBase+this.adicionalSalario);
    }

}

enum Vinculo {
    CLT,
    PJ,
    ESTAGIO,
}

enum Cargo {
    EXECUTIVO,
    ENGENHEIRO,
    ANALISTA,
    TECNICO,
    ESTAGIARIO,
}

enum Departamento {
    RH,
    ENGENHARIA,
    DIRETORIA,
}

class Funcionario {
    Pessoa pessoa;
    Financeiro financeiro;
    Cargo cargo;
    Departamento departamento;
    boolean ativo;
    
    public Funcionario(Pessoa pessoa, Financeiro financeiro, Cargo cargo, Departamento departamento, boolean ativo) {
        this.pessoa = pessoa;
        this.financeiro = financeiro;
        this.cargo = cargo;
        this.departamento = departamento;
        this.ativo = ativo;
    }
    public boolean getAtivo() {
        return this.ativo;
    }
    public void setAtivo(boolean ativo){
        this.ativo = ativo;
    }
    
}

class FolhaPagamento {
    ArrayList<Funcionario> listaFuncionario = new ArrayList<Funcionario>();
    ArrayList<String> folhaPagamento = new ArrayList<String>();
    ArrayList<String> relatorio = new ArrayList<String>();
    public void gerarFolhaPagamento() {
        
    }
    public void gerarRelatorio() {
        
    }

    public void exportarRelatorio() {
        
    }
    
}