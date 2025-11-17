package ui;

import Candidatura.ui.MenuCandidatura;
import Financeiro.ui.TelaFinanceiro;
import AdministracaoGestao.ui.MenuGestao;
import java.awt.*;
import javax.swing.*;

/**
 * Menu principal da aplicação.
 */
public class MenuPrincipal extends JFrame {

    private JButton btnAdminGestao;
    private JButton btnFinanceiro;
    private JButton btnRecrutamento;
    private JButton btnCadastrarUsuario; // NOVO CAMPO
    private JButton sairButton;

    public MenuPrincipal() {
        setTitle("Sistema de Gestão Empresarial (HR)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lbl = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);

        // Layout para 5 botões
        JPanel grid = new JPanel(new GridLayout(5, 1, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        btnAdminGestao = new JButton("1. Administração e Gestão (Em Construção)");
        btnFinanceiro = new JButton("2. Módulo Financeiro");
        btnRecrutamento = new JButton("3. Recrutamento e Candidatura");
        btnCadastrarUsuario = new JButton("4. Cadastrar Novo Usuário (RH/Admin)"); // NOVO

        //btnAdminGestao.setEnabled(false);

        grid.add(btnAdminGestao);
        grid.add(btnFinanceiro);
        grid.add(btnRecrutamento);
        grid.add(btnCadastrarUsuario); // Adicionado

        add(grid, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        sairButton = new JButton("Sair do Sistema");
        footer.add(sairButton);
        add(footer, BorderLayout.SOUTH);

        configurarListeners();

        pack();
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    private void configurarListeners() {
        btnAdminGestao.addActionListener(e -> abrirModuloGestao());
        btnFinanceiro.addActionListener(e -> abrirModuloFinanceiro());
        btnRecrutamento.addActionListener(e -> abrirModuloCandidatura());
        btnCadastrarUsuario.addActionListener(e -> abrirCadastroUsuario()); // NOVO LISTENER
        sairButton.addActionListener(e -> dispose());
    }

    private void abrirModuloCandidatura() {
        MenuCandidatura menuCandidatura = new MenuCandidatura();
        menuCandidatura.setVisible(true);
    }

    private void abrirCadastroUsuario() {
        TelaCadastroUsuario tela = new TelaCadastroUsuario();
        tela.setVisible(true);
    }

    private void abrirModuloFinanceiro() {
        TelaFinanceiro tela = new TelaFinanceiro();
        tela.setVisible(true);
    }

    private void abrirModuloGestao() {
        MenuGestao tela = new MenuGestao();
        tela.setVisible(true);
    }
}