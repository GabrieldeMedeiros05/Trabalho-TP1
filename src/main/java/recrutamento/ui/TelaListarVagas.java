package recrutamento.ui;

import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela de listagem e filtro de vagas.
 */
public class TelaListarVagas extends JFrame {

    private final RecrutamentoService service;

    // Filtros
    private JTextField txtCargo;
    private JTextField txtDepartamento;
    private JComboBox<Object> comboStatus;
    private JComboBox<Object> comboRegime;
    private JTextField txtSalarioMin;
    private JTextField txtSalarioMax;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;

    // Tabela
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Vaga> vagasAtual = new ArrayList<>();

    // Botões
    private JButton btnFiltrar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnFechar;

    private final DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaListarVagas(RecrutamentoService service) {
        this.service = service;

        setTitle("Vagas - Listar / Filtrar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(8, 8));

        montarComponentes();
        configurarAcoes();
        carregarTodasVagas();
    }

    private void montarComponentes() {
        JPanel painelFiltros = new JPanel(new GridBagLayout());
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int linha = 0;

        // Cargo
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelFiltros.add(new JLabel("Cargo:"), gbc);

        gbc.gridx = 1;
        txtCargo = new JTextField();
        painelFiltros.add(txtCargo, gbc);

        // Departamento
        gbc.gridx = 2;
        painelFiltros.add(new JLabel("Departamento:"), gbc);

        gbc.gridx = 3;
        txtDepartamento = new JTextField();
        painelFiltros.add(txtDepartamento, gbc);

        // Status
        linha++;
        gbc.gridy = linha;
        gbc.gridx = 0;
        painelFiltros.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        comboStatus = new JComboBox<>();
        comboStatus.addItem("Todos");
        for (StatusVaga s : StatusVaga.values()) {
            comboStatus.addItem(s);
        }
        painelFiltros.add(comboStatus, gbc);

        // Regime
        gbc.gridx = 2;
        painelFiltros.add(new JLabel("Regime:"), gbc);

        gbc.gridx = 3;
        comboRegime = new JComboBox<>();
        comboRegime.addItem("Todos");
        for (RegimeContratacao r : RegimeContratacao.values()) {
            comboRegime.addItem(r);
        }
        painelFiltros.add(comboRegime, gbc);

        // Faixa salarial
        linha++;
        gbc.gridy = linha;
        gbc.gridx = 0;
        painelFiltros.add(new JLabel("Salário mín. (R$):"), gbc);

        gbc.gridx = 1;
        txtSalarioMin = new JTextField();
        painelFiltros.add(txtSalarioMin, gbc);

        gbc.gridx = 2;
        painelFiltros.add(new JLabel("Salário máx. (R$):"), gbc);

        gbc.gridx = 3;
        txtSalarioMax = new JTextField();
        painelFiltros.add(txtSalarioMax, gbc);

        // Data abertura
        linha++;
        gbc.gridy = linha;
        gbc.gridx = 0;
        painelFiltros.add(new JLabel("Data início (dd/MM/aaaa):"), gbc);

        gbc.gridx = 1;
        txtDataInicio = new JTextField();
        painelFiltros.add(txtDataInicio, gbc);

        gbc.gridx = 2;
        painelFiltros.add(new JLabel("Data fim (dd/MM/aaaa):"), gbc);

        gbc.gridx = 3;
        txtDataFim = new JTextField();
        painelFiltros.add(txtDataFim, gbc);

        // Botão filtrar
        btnFiltrar = new JButton("Aplicar filtros");
        JPanel painelTop = new JPanel(new BorderLayout());
        painelTop.add(painelFiltros, BorderLayout.CENTER);
        painelTop.add(btnFiltrar, BorderLayout.EAST);

        add(painelTop, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {
                "ID interno",
                "Código",
                "Cargo",
                "Departamento",
                "Regime",
                "Salário Base",
                "Status",
                "Data Abertura"
        };

        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        // Botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnFechar = new JButton("Fechar");

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarAcoes() {
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnFechar.addActionListener(e -> dispose());

        btnExcluir.addActionListener(e -> excluirSelecionada());

        btnEditar.addActionListener(e -> abrirEdicao());
    }

    private void carregarTodasVagas() {
        try {
            vagasAtual = service.listarVagas();
            preencherTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar vagas.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void preencherTabela() {
        modeloTabela.setRowCount(0);

        for (Vaga v : vagasAtual) {
            String data = v.getDataAbertura() != null
                    ? v.getDataAbertura().format(fmtData)
                    : "";
            modeloTabela.addRow(new Object[]{
                    v.getId(),
                    v.getCodigo(),
                    v.getCargo(),
                    v.getDepartamento(),
                    v.getRegime(),
                    v.getSalarioBase(),
                    v.getStatus(),
                    data
            });
        }
    }

    private void aplicarFiltros() {
        try {
            String cargo = txtCargo.getText().trim();
            if (cargo.isEmpty()) cargo = null;

            String departamento = txtDepartamento.getText().trim();
            if (departamento.isEmpty()) departamento = null;

            StatusVaga status = null;
            Object selStatus = comboStatus.getSelectedItem();
            if (selStatus instanceof StatusVaga) {
                status = (StatusVaga) selStatus;
            }

            RegimeContratacao regime = null;
            Object selRegime = comboRegime.getSelectedItem();
            if (selRegime instanceof RegimeContratacao) {
                regime = (RegimeContratacao) selRegime;
            }

            Double faixaMin = parseDoubleOrNull(txtSalarioMin.getText());
            Double faixaMax = parseDoubleOrNull(txtSalarioMax.getText());

            LocalDate dataInicio = parseDataOuNull(txtDataInicio.getText());
            LocalDate dataFim = parseDataOuNull(txtDataFim.getText());

            vagasAtual = service.filtrarVagas(
                    cargo,
                    departamento,
                    status,
                    regime,
                    faixaMin,
                    faixaMax,
                    dataInicio,
                    dataFim
            );

            preencherTabela();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de negócio",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao aplicar filtros.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            return Double.parseDouble(s.replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Valor numérico inválido: " + s,
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private LocalDate parseDataOuNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s, fmtData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida: " + s + " (use dd/MM/aaaa)",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Vaga obterVagaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma vaga na tabela.",
                    "Nenhuma vaga selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (linha >= vagasAtual.size()) {
            return null;
        }
        return vagasAtual.get(linha);
    }

    private void excluirSelecionada() {
        Vaga vaga = obterVagaSelecionada();
        if (vaga == null) return;

        int opc = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir a vaga?\n" + vaga.getCargo(),
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc != JOptionPane.YES_OPTION) return;

        try {
            service.excluirVaga(vaga.getId());
            JOptionPane.showMessageDialog(this,
                    "Vaga excluída com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            aplicarFiltros(); // recarrega lista com filtros atuais

        } catch (AutorizacaoException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de autorização",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de negócio",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado ao excluir vaga.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void abrirEdicao() {
        Vaga vaga = obterVagaSelecionada();
        if (vaga == null) {
            return;
        }

        TelaCadastroVaga tela = new TelaCadastroVaga(service, vaga);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

}
