package Candidatura.ui;

import Candidatura.dominio.Candidato;
import Candidatura.excecoes.RegraNegocioException;
import Candidatura.servico.CandidaturaService;
import Seguranca.servico.UsuarioService; // Importa o serviço de segurança
import utils.AppConfig; // Importa a configuração global

import javax.swing.*;
import java.awt.*;

/**
 * Tela para cadastrar candidatos (salva em data/candidatos.csv).
 * Agora também cria um usuário de login.
 */
public class TelaCadastroCandidato extends JFrame {

    // Injeção de dependência via AppConfig
    private final CandidaturaService candidaturaService = CandidaturaModuleConfig.candidaturaService();
    private final UsuarioService usuarioService = AppConfig.usuarioService();

    private JTextField txtCpf;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTextField txtLinkCurriculo;
    private JTextField txtPerfilProfissional;
    private JTextArea txtCurriculo;
    private JTextField txtLogin;
    private JPasswordField txtSenha;

    public TelaCadastroCandidato() {
        setTitle("Cadastro de Candidato");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Layout de 9 linhas (CPF, Nome, Email, Tel, Link, Perfil, Currículo, Login, Senha)
        JPanel form = new JPanel(new GridLayout(9, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        form.add(txtCpf);

        form.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        form.add(txtNome);

        form.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        form.add(txtEmail);

        form.add(new JLabel("Telefone (Opcional):"));
        txtTelefone = new JTextField();
        form.add(txtTelefone);

        form.add(new JLabel("Link Currículo (Opcional):"));
        txtLinkCurriculo = new JTextField();
        form.add(txtLinkCurriculo);

        form.add(new JLabel("Perfil Profissional:"));
        txtPerfilProfissional = new JTextField();
        form.add(txtPerfilProfissional);

        form.add(new JLabel("Resumo Curriculo:"));
        txtCurriculo = new JTextArea(3, 15);
        form.add(new JScrollPane(txtCurriculo)); // Usa JScrollPane para área de texto

        form.add(new JLabel("Login (Acesso):"));
        txtLogin = new JTextField();
        form.add(txtLogin);

        form.add(new JLabel("Senha (Acesso):"));
        txtSenha = new JPasswordField();
        form.add(txtSenha);


        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setSize(550, 480);
        setLocationRelativeTo(null);
    }

    private void salvar() {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String linkCurriculo = txtLinkCurriculo.getText().trim();
        String perfilProfissional = txtPerfilProfissional.getText().trim();
        String curriculo = txtCurriculo.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (cpf.isEmpty() || nome.isEmpty() || email.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha CPF, Nome, Email, Login e Senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // --- NOVO PASSO: VALIDAÇÃO DO CPF ---
            candidaturaService.validarCpf(cpf); // Lança RegraNegocioException se inválido

            // 1. Cria e salva o Candidato (Pessoa)
            Candidato c = new Candidato(cpf, nome, email);
            if (!telefone.isEmpty()) c.setTelefone(telefone);
            if (!linkCurriculo.isEmpty()) c.setLinkCurriculo(linkCurriculo);
            if (!perfilProfissional.isEmpty()) c.setPerfilProfissional(perfilProfissional);
            if (!curriculo.isEmpty()) c.setCurriculo(curriculo);

            candidaturaService.salvarCandidato(c);

            // 2. Cria e salva o Usuário associado ao CPF (login/senha)
            usuarioService.criarUsuarioCandidato(login, senha, cpf);

            JOptionPane.showMessageDialog(this, "Candidato e Usuário salvos com sucesso. Use o login/senha para acessar!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (RegraNegocioException ex) {
            // Captura erros de validação (Regra de Negócio/CPF)
            JOptionPane.showMessageDialog(this, "Falha no Cadastro: " + ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Captura erros de Persistência ou outros
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}