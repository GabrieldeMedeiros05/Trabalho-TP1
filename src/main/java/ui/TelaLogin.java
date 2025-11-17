package ui;

import Seguranca.dominio.Usuario;
import Candidatura.excecoes.AutorizacaoException;
import Seguranca.servico.UsuarioService;
import Candidatura.ui.MenuCandidato; // Importa o novo menu do candidato
import Candidatura.ui.TelaVerMinhasCandidaturas; // Tela de visualização
import utils.AppConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login unificada para o sistema.
 */
public class TelaLogin extends JFrame {

    private final UsuarioService usuarioService = AppConfig.usuarioService();

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        setTitle("Acesso ao Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Login:"));
        txtLogin = new JTextField(15);
        form.add(txtLogin);

        form.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField(15);
        form.add(txtSenha);

        btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> tentarLogin());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(btnEntrar);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        pack();
        setSize(350, 200);
        setLocationRelativeTo(null);
    }

    private void tentarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        try {
            Usuario usuario = usuarioService.autenticar(login, senha);
            JOptionPane.showMessageDialog(this, "Login efetuado com sucesso como: " + usuario.getTipo(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            dispose();

            if ("CANDIDATO".equalsIgnoreCase(usuario.getTipo())) {
                // REDIRECIONA PARA O MENU ESPECÍFICO DO CANDIDATO
                new MenuCandidato(usuario.getCpf_cnpj()).setVisible(true);
            } else {
                // REDIRECIONA PARA O MENU GERAL (RH/ADMIN)
                new MenuPrincipal(usuario).setVisible(true);
            }

        } catch (AutorizacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}