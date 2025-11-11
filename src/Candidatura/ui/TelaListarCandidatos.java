package Candidatura.ui;

import Candidatura.dominio.Candidato;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tela que lista candidatos salvos. Agora também permite Busca (CPF/Nome) e Exclusão.
 */
public class TelaListarCandidatos extends JFrame {

    private final CandidaturaService service = CandidaturaModuleConfig.candidaturaService();

    private JTable tabela;
    private DefaultTableModel model;
    private JTextField txtFiltroCpf;
    private JTextField txtFiltroNome;

    public TelaListarCandidatos() {
        setTitle("Candidatos: Listar / Consultar / Deletar");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // --- 1. Painel de Busca (NORTH) ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        txtFiltroCpf = new JTextField(12);
        txtFiltroNome = new JTextField(15);
        JButton btnBuscar = new JButton("Filtrar");
        JButton btnLimpar = new JButton("Listar Todos");

        painelBusca.add(new JLabel("CPF:"));
        painelBusca.add(txtFiltroCpf);
        painelBusca.add(new JLabel("Nome:"));
        painelBusca.add(txtFiltroNome);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnLimpar);

        add(painelBusca, BorderLayout.NORTH);

        // --- 2. Tabela (CENTER) ---
        String[] colunas = {"CPF", "Nome", "Email", "Telefone", "Currículo"};
        model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- 3. Rodapé com Botões de Ação (SOUTH) ---
        JButton btnFechar = new JButton("Fechar");
        JButton btnDeletar = new JButton("Deletar Candidato Selecionado");

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(btnDeletar);
        footer.add(btnFechar);
        add(footer, BorderLayout.SOUTH);

        // --- 4. Listeners ---
        btnBuscar.addActionListener(e -> carregar(true));
        btnLimpar.addActionListener(e -> {
            txtFiltroCpf.setText("");
            txtFiltroNome.setText("");
            carregar(false);
        });
        btnFechar.addActionListener(e -> dispose());
        btnDeletar.addActionListener(e -> deletarCandidato());

        pack();
        setSize(900, 500);
        setLocationRelativeTo(null);

        carregar(false);
    }

    private void carregar(boolean usarFiltro) {
        model.setRowCount(0);
        try {
            List<Candidato> lista = service.listarCandidatos();

            String filtroCpf = txtFiltroCpf.getText().trim();
            String filtroNome = txtFiltroNome.getText().trim().toLowerCase();

            if (usarFiltro && (!filtroCpf.isEmpty() || !filtroNome.isEmpty())) {
                lista = lista.stream()
                        // CORREÇÃO AQUI
                        .filter(c -> filtroCpf.isEmpty() || c.getCpf_cnpj().contains(filtroCpf))
                        .filter(c -> filtroNome.isEmpty() || c.getNome().toLowerCase().contains(filtroNome))
                        .collect(Collectors.toList());
            }

            for (Candidato c : lista) {
                model.addRow(new Object[]{
                        // CORREÇÃO AQUI
                        c.getCpf_cnpj(), c.getNome(), c.getEmail(),
                        c.getTelefone() != null ? c.getTelefone() : "-",
                        c.getLinkCurriculo() != null ? c.getLinkCurriculo() : "-"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidatos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCandidato() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CPF é lido da primeira coluna, que agora é c.getCpf_cnpj()
        String cpf = (String) model.getValueAt(linhaSelecionada, 0);
        String nome = (String) model.getValueAt(linhaSelecionada, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja deletar o candidato:\n" + nome + " (" + cpf + ")?\n" +
                        "Esta ação é irreversível e só será permitida se não houver candidaturas vinculadas.",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Service ainda espera 'cpf' como parâmetro, o que está correto
                if (service.deletarCandidato(cpf)) {
                    JOptionPane.showMessageDialog(this, "Candidato excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregar(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível encontrar ou deletar o candidato.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}