package Financeiro.ui;

import Financeiro.FinanceiroTableModel;
import Financeiro.dados.LeitorFuncionario;
import Seguranca.dominio.Funcionario;
import utils.Constantes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TelaFinanceiro extends JFrame {

    private JTable tabela;
    private JTextField txtFiltroLivre;
    private JTextField txtFiltroCargo;
    private JTextField txtFiltroTipoContratacao;
    private JTextField txtFiltroStatus;
    private JTextField txtFiltroDepartamento;

    public TelaFinanceiro() {
        List<Funcionario> funcionarios = new LeitorFuncionario().retornaLista();
        FinanceiroTableModel model = new FinanceiroTableModel(funcionarios);
        Set<String> cargo = funcionarios.stream().map(funcionario -> funcionario.getCargo()).collect(Collectors.toSet());
        Set<String> tipoContrato = funcionarios.stream().map(funcionario -> funcionario.getTipoContrato()).collect(Collectors.toSet());
        Set<String> status = funcionarios.stream().map(funcionario -> funcionario.getStatus()).collect(Collectors.toSet());
        Set<String> departamentos = funcionarios.stream().map(funcionario -> funcionario.getDepartamento()).collect(Collectors.toSet());
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        status.add("");
        cargo.add("");
        tipoContrato.add("");
        status.add("");
        departamentos.add("");
        setTitle("Financeiro: Funcionários");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // NORTE
        txtFiltroLivre = new JTextField(25);
        JComboBox<String> dropdownTipoCargo = new JComboBox<>(cargo.toArray(new String[0]));
        JComboBox<String> dropdownTipoContratacao = new JComboBox<>(tipoContrato.toArray(new String[0]));
        JComboBox<String> dropdownStatus = new JComboBox<>(status.toArray(new String[0]));
        JComboBox<String> dropdownDepartamento = new JComboBox<>(departamentos.toArray(new String[0]));

        JPanel botoesEsquerda = new JPanel();
        JPanel botoesDireita = new JPanel();
        JPanel painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnLimpar = new JButton("Limpar");

        botoesEsquerda.add(new JLabel("Busca Livre:"));
        botoesEsquerda.add(txtFiltroLivre);
        botoesEsquerda.add(new JLabel("Cargo:"));
        botoesEsquerda.add(dropdownTipoCargo);
        botoesEsquerda.add(new JLabel("Regime:"));
        botoesEsquerda.add(dropdownTipoContratacao);
        botoesEsquerda.add(new JLabel("Status:"));
        botoesEsquerda.add(dropdownStatus);
        botoesEsquerda.add(new JLabel("Departamento:"));
        botoesEsquerda.add(dropdownDepartamento);

        botoesDireita.add(btnLimpar, BorderLayout.EAST);

        painelBusca.add(botoesEsquerda, BorderLayout.WEST);
        painelBusca.add(botoesDireita, BorderLayout.EAST);
        add(painelBusca, BorderLayout.NORTH);

        // CENTRO
        txtFiltroLivre.getDocument().addDocumentListener(new DocumentListener() {
            private void applyFilter() {
                String text = txtFiltroLivre.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        dropdownTipoCargo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemSelecionado = (String) dropdownTipoCargo.getSelectedItem();
                if (itemSelecionado == null || "Todas".equals(itemSelecionado) || itemSelecionado.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + itemSelecionado + "$", 2));
                }
            }
        });
        dropdownTipoContratacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemSelecionado = (String) dropdownTipoContratacao.getSelectedItem();
                if (itemSelecionado == null || "Todas".equals(itemSelecionado) || itemSelecionado.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + itemSelecionado + "$", 5));
                }
            }
        });
        dropdownStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemSelecionado = (String) dropdownStatus.getSelectedItem();
                if (itemSelecionado == null || "Todas".equals(itemSelecionado) || itemSelecionado.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + itemSelecionado + "$", 4));
                }
            }
        });
        dropdownDepartamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemSelecionado = (String) dropdownDepartamento.getSelectedItem();
                if (itemSelecionado == null || "Todas".equals(itemSelecionado) || itemSelecionado.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("^" + itemSelecionado + "$", 3));
                }
            }
        });

        tabela = new JTable(model);
        tabela.setRowSorter(sorter);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // SUL
        botoesEsquerda = new JPanel();
        botoesDireita = new JPanel();
        painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnFechar = new JButton("Fechar");
        JButton btnGerarFolha = new JButton("Gerar Folha");

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        botoesEsquerda.add(btnGerarFolha);
        botoesDireita.add(btnFechar);

        footer.add(botoesEsquerda, BorderLayout.WEST);
        footer.add(botoesDireita, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        // Listeners
        btnFechar.addActionListener(e -> dispose());
        btnGerarFolha.addActionListener(e -> abrirFolhaPagamento());
        btnLimpar.addActionListener(e -> {
            txtFiltroLivre.setText("");
            dropdownTipoCargo.setSelectedIndex(0);
            dropdownTipoContratacao.setSelectedIndex(0);
            dropdownStatus.setSelectedIndex(0);
            dropdownDepartamento.setSelectedIndex(0);
        });

        pack();
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);

    }

    private void abrirFolhaPagamento() {
        TelaFolhaPagamento tela = new TelaFolhaPagamento();
        tela.setVisible(true);
//        this.setVisible(false);
    }

}
