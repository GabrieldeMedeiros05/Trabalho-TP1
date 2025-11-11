package Candidatura.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Menu principal para usuários do tipo CANDIDATO.
 */
public class MenuCandidato extends JFrame {

    private JButton btnVerMinhasCandidaturas;
    private JButton btnListarTodasCandidaturas;
    private JButton btnSair;

    private final String cpfCandidato;

    public MenuCandidato(String cpfCandidato) {
        this.cpfCandidato = cpfCandidato;
        setTitle("Área do Candidato - CPF: " + cpfCandidato);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("MENU DO CANDIDATO", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lbl, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 1, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnVerMinhasCandidaturas = new JButton("1. Ver Minhas Candidaturas");


        grid.add(btnVerMinhasCandidaturas);


        add(grid, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        btnSair = new JButton("Sair do Sistema");
        footer.add(btnSair);
        add(footer, BorderLayout.SOUTH);

        configurarListeners();

        pack();
        setSize(420, 300);
        setLocationRelativeTo(null);
    }

    private void abrir(JFrame f) {
        f.setLocationRelativeTo(this);
        f.setVisible(true);
    }

    private void configurarListeners() {

        btnVerMinhasCandidaturas.addActionListener(e -> abrir(new TelaVerMinhasCandidaturas(cpfCandidato)));

        btnSair.addActionListener(e -> System.exit(0));
    }
}