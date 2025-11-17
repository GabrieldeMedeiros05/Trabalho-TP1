package Candidatura.ui;

import Candidatura.dominio.Candidato;
import Candidatura.dominio.Candidatura;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Tela para consultar um Candidato por CPF e listar suas candidaturas.
 */
public class TelaConsultaCandidato extends JFrame {

    private final CandidaturaService service = CandidaturaModuleConfig.service();

    private JTextField txtCpfBusca;
    private JLabel lblNomeValor;
    private JLabel lblEmailValor;
    private JLabel lblTelefoneValor;
    private JLabel lblCurriculoValor;
    private JPanel painelInfo;

    private JTable tabelaCandidaturas;
    private DefaultTableModel modelCandidaturas;

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaConsultaCandidato() {
        setTitle("Consulta de Candidato");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. Painel de Busca (Topo)
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCpfBusca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar por CPF");

        painelBusca.add(new JLabel("CPF:"));
        painelBusca.add(txtCpfBusca);
        painelBusca.add(btnBuscar);

        add(painelBusca, BorderLayout.NORTH);

        // 2. Painel Central (Informações do Candidato e Candidaturas)
        JPanel painelCentral = new JPanel(new GridLayout(2, 1, 10, 10));

        // 2.1 Informações do Candidato
        painelInfo = criarPainelInformacoes();
        painelCentral.add(painelInfo);

        // 2.2 Candidaturas
        modelCandidaturas = new DefaultTableModel(new String[]{"ID Candidatura", "Vaga", "Status", "Data"}, 0);
        tabelaCandidaturas = new JTable(modelCandidaturas);
        JScrollPane scrollCandidaturas = new JScrollPane(tabelaCandidaturas);
        scrollCandidaturas.setBorder(BorderFactory.createTitledBorder("Candidaturas Registradas"));
        painelCentral.add(scrollCandidaturas);

        add(painelCentral, BorderLayout.CENTER);

        // 3. Listener e Configurações Finais
        btnBuscar.addActionListener(e -> buscarCandidato());

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelInformacoes() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Candidato"));

        painel.add(new JLabel("Nome:"));
        lblNomeValor = new JLabel("N/A");
        painel.add(lblNomeValor);

        painel.add(new JLabel("Email:"));
        lblEmailValor = new JLabel("N/A");
        painel.add(lblEmailValor);

        painel.add(new JLabel("Telefone:"));
        lblTelefoneValor = new JLabel("N/A");
        painel.add(lblTelefoneValor);

        painel.add(new JLabel("Link Currículo:"));
        lblCurriculoValor = new JLabel("N/A");
        lblCurriculoValor.setToolTipText("Clique para abrir o link (Funcionalidade extra)"); // Dica visual
        painel.add(lblCurriculoValor);

        return painel;
    }

    private void buscarCandidato() {
        String cpf = txtCpfBusca.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o CPF para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<Candidato> optCandidato = service.buscarCandidatoPorCpf(cpf);

            if (optCandidato.isPresent()) {
                Candidato c = optCandidato.get();
                // 1. Atualiza Dados do Candidato
                lblNomeValor.setText(c.getNome());
                lblEmailValor.setText(c.getEmail());
                lblTelefoneValor.setText(c.getTelefone() != null && !c.getTelefone().isEmpty() ? c.getTelefone() : "-");
                lblCurriculoValor.setText(c.getLinkCurriculo() != null && !c.getLinkCurriculo().isEmpty() ? c.getLinkCurriculo() : "-");

                // 2. Carrega Candidaturas
                carregarCandidaturas(cpf);

            } else {
                // Candidato não encontrado
                limparCampos();
                JOptionPane.showMessageDialog(this, "Candidato com CPF " + cpf + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            limparCampos();
            JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCandidaturas(String cpf) {
        modelCandidaturas.setRowCount(0); // Limpa a tabela
        try {
            // Chama o novo método no Service para buscar candidaturas por CPF
            List<Candidatura> candidaturas = service.listarCandidaturasPorCpf(cpf);

            for (Candidatura c : candidaturas) {
                modelCandidaturas.addRow(new Object[]{
                        c.getId().substring(0, 8) + "...", // Mostrar só o começo do ID
                        c.getVagaId(),
                        c.getStatus().name(),
                        c.getDataCriacao().format(FORMATADOR)
                });
            }
        } catch (Exception ex) {
            // Tratamento de erro específico para a tabela
        }
    }

    private void limparCampos() {
        lblNomeValor.setText("N/A");
        lblEmailValor.setText("N/A");
        lblTelefoneValor.setText("N/A");
        lblCurriculoValor.setText("N/A");
        modelCandidaturas.setRowCount(0);
    }
}