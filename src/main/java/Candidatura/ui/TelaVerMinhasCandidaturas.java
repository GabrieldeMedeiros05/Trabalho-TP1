package Candidatura.ui;

import Candidatura.dominio.Candidatura;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela que lista APENAS as candidaturas do usuário logado.
 */
public class TelaVerMinhasCandidaturas extends JFrame {

    private final CandidaturaService candidaturaService = CandidaturaModuleConfig.candidaturaService();
    private final String cpfCandidato;

    private JTable tabela;
    private DefaultTableModel model;
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaVerMinhasCandidaturas(String cpfCandidato) {
        this.cpfCandidato = cpfCandidato;
        setTitle("Minhas Candidaturas (CPF: " + cpfCandidato + ")");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Muda para DISPOSE_ON_CLOSE para não fechar o Menu Candidato
        setLayout(new BorderLayout(8, 8));

        JLabel lbl = new JLabel("Visão do Candidato: Vagas em que você se inscreveu", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lbl, BorderLayout.NORTH);

        String[] colunas = {"ID Vaga", "Status", "Data da Inscrição"};
        model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(model);
        tabela.setRowSelectionAllowed(false);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose()); // Fecha apenas esta tela

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(btnFechar);
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