package Candidatura.ui;

import Candidatura.dominio.Candidatura;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela de visualização exclusiva para o Candidato logado.
 */
public class TelaCandidatoHome extends JFrame {

    private final CandidaturaService candidaturaService = CandidaturaModuleConfig.candidaturaService();
    private final String cpfCandidato;

    private JTable tabela;
    private DefaultTableModel model;
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCandidatoHome(String cpfCandidato) {
        this.cpfCandidato = cpfCandidato;
        setTitle("Minhas Candidaturas (CPF: " + cpfCandidato + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel lbl = new JLabel("Visão do Candidato: Vagas em que você se inscreveu", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lbl, BorderLayout.NORTH);

        // Tabela de Candidaturas
        String[] colunas = {"ID Vaga", "Status", "Data da Inscrição"};
        model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(model);
        tabela.setRowSelectionAllowed(false);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(btnSair);
        add(footer, BorderLayout.SOUTH);

        pack();
        setSize(700, 400);
        setLocationRelativeTo(null);

        carregarCandidaturas();
    }

    private void carregarCandidaturas() {
        model.setRowCount(0);
        try {
            List<Candidatura> lista = candidaturaService.listarCandidaturasPorCpf(this.cpfCandidato);

            if (lista.isEmpty()) {
                model.addRow(new Object[]{"Nenhuma candidatura encontrada.", "", ""});
            } else {
                for (Candidatura c : lista) {
                    model.addRow(new Object[]{
                            c.getVagaId(),
                            c.getStatus().name(),
                            c.getDataCriacao().format(FORMATADOR)
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidaturas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}