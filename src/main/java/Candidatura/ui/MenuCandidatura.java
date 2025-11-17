package Candidatura.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Menu principal do módulo Candidatura.
 */
public class MenuCandidatura extends JFrame {

    private JButton btnCandidatosCadastrar;
    private JButton btnCandidatosListar;
    private JButton btnCandidaturaRealizar;
    private JButton sairButton;

    public MenuCandidatura() {
        setTitle("Candidatura • Menu");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Menu de Candidatura", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lbl, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(5, 1, 10, 10)); // 5 Linhas
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnCandidatosCadastrar = new JButton("Candidatos – Cadastrar");
        btnCandidatosListar = new JButton("Candidatos – Listar e Consultar");
        btnCandidaturaRealizar = new JButton("Candidatura – Realizar");


        grid.add(btnCandidatosCadastrar);
        grid.add(btnCandidatosListar);
        grid.add(btnCandidaturaRealizar);

        add(grid, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        sairButton = new JButton("Sair");
        footer.add(sairButton);
        add(footer, BorderLayout.SOUTH);

        configurarListeners();

        pack();
        setSize(450, 400);
        setLocationRelativeTo(null);
    }

    private void abrir(JFrame f) {
        f.setLocationRelativeTo(this);
        f.setVisible(true);
    }

    private void configurarListeners() {
        btnCandidatosCadastrar.addActionListener(e -> abrir(new TelaCadastroCandidato()));
        btnCandidatosListar.addActionListener(e -> abrir(new TelaListarCandidatos()));
        btnCandidaturaRealizar.addActionListener(e -> abrir(new TelaRealizarCandidatura()));

        sairButton.addActionListener(e -> dispose());
    }

    public void exibir() {
        // ...
    }
}