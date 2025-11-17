package ui;

import Seguranca.dominio.Usuario;
import Candidatura.excecoes.RegraNegocioException;
import Seguranca.servico.UsuarioService;
import utils.AppConfig; // Importa a configuração global de segurança

import javax.swing.*;
import java.awt.*;

/**
 * Tela para cadastrar usuários internos (RH, Admin, Gestores) no sistema.
 * Acessada via MenuPrincipal.
 */
public class TelaCadastroUsuario extends JFrame {

    // Injeção do serviço de segurança
    private final UsuarioService usuarioService = AppConfig.usuarioService();

    private JTextField txtNome;
    private JTextField txtCpfCnpj;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cmbTipo;

    public TelaCadastroUsuario() {
        setTitle("Cadastro de Usuário Interno");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Layout para 5 pares de campos (Nome, CPF, Login, Senha, Tipo)
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        form.add(txtNome);

        form.add(new JLabel("CPF/CNPJ:"));
        txtCpfCnpj = new JTextField();
        form.add(txtCpfCnpj);

        form.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        form.add(txtLogin);

        form.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        form.add(txtSenha);

        form.add(new JLabel("Tipo de Usuário:"));
        cmbTipo = new JComboBox<>(new String[]{"RH", "ADMIN", "GESTOR"});
        form.add(cmbTipo);


        JButton btnSalvar = new JButton("Salvar Usuário");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setSize(450, 300);
        setLocationRelativeTo(null);
    }

    private void salvarUsuario() {
        String nome = txtNome.getText().trim();
        String cpfCnpj = txtCpfCnpj.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nome.isEmpty() || cpfCnpj.isEmpty() || login.isEmpty() || senha.isEmpty() || tipo == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Define o departamento baseado no tipo de usuário
            String departamento = tipo.equals("RH") ? "RECRUTAMENTO" : "ADMINISTRACAO";

            // Cria a entidade Usuario (ID = 0L, será gerado na persistência)
            Usuario novoUsuario = new Usuario(
                    0L,
                    login,
                    senha,
                    tipo,
                    nome,
                    cpfCnpj,
                    "ATIVO",
                    departamento
            );

            usuarioService.cadastrarUsuario(novoUsuario);

            JOptionPane.showMessageDialog(this,
                    "Usuário " + tipo + " salvo com sucesso! Login: " + login,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}