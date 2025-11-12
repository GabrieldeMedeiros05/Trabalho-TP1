package Financeiro;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import Seguranca.dominio.Funcionario;


public class FuncionarioTableModel extends AbstractTableModel {

    private List<Funcionario> funcionarios;
    private String[] colunas = {"Matrícula", "Nome", "Cargo", "Departamento", "Salário"};

    public FuncionarioTableModel(List<Funcionario> dados) {
        this.funcionarios = dados;
    }

    public void setFuncionarios(List<Funcionario> novosDados) {
        this.funcionarios = novosDados;
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public int getRowCount() {
        return funcionarios.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Funcionario f = funcionarios.get(rowIndex);

        switch (columnIndex) {
            case 0: return f.getMatricula();
            case 1: return f.getNome();
            case 2: return f.getCargo();
            case 3: return f.getDepartamento();
            case 4: return f.getSalarioBase();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}