package AdministracaoGestao.modelos;

import Seguranca.dominio.Usuario;

import javax.swing.table.AbstractTableModel;
import java.util.List;


public class UsuariosTableModel extends AbstractTableModel {

    private List<Usuario> usuarios;
    private String[] colunas = {"Nome", "CPF", "Senha", "Tipo", "Status", "Departamento"};

    public UsuariosTableModel(List<Usuario> dados) {
        this.usuarios = dados;
    }

    public void setFuncionarios(List<Usuario> novosDados) {
        this.usuarios = novosDados;
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
        return usuarios.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario f = usuarios.get(rowIndex);

        switch (columnIndex) {
            case 0: return f.getNome();
            case 1: return f.getCpf_cnpj();
            case 2: return f.getLogin();
            case 3: return f.getSenha();
            case 4: return f.getTipo();
            case 5: return f.getStatus();
            case 6: return f.getDepartamento();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}