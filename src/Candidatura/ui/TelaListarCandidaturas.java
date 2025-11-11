package Candidatura.ui;

import Candidatura.dominio.Candidato;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import java.awt.*;

/**
 * Tela para cadastrar candidatos (salva em data/candidatos.csv).
 */
public class TelaListarCandidaturas extends JFrame {

    private final CandidaturaService service = CandidaturaModuleConfig.service();

    private JTextField txtCpf;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone; // NOVO
    private JTextField txtLinkCurriculo; // NOVO

    public TelaListarCandidaturas() {
        setTitle("Cadastro de Candidato");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Layout de 5 linhas para os 5 campos (2 colunas)
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
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

        // Novos campos
        form.add(new JLabel("Telefone (Opcional):"));
        txtTelefone = new JTextField();
        form.add(txtTelefone);

        form.add(new JLabel("Link Currículo (Opcional):"));
        txtLinkCurriculo = new JTextField();
        form.add(txtLinkCurriculo);


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
        setSize(420, 300); // Ajuste de tamanho
        setLocationRelativeTo(null);
    }

    private void salvar() {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String linkCurriculo = txtLinkCurriculo.getText().trim();

        if (cpf.isEmpty() || nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha CPF, Nome e Email.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Candidato c = new Candidato(cpf, nome, email);

            // Adiciona campos opcionais
            if (!telefone.isEmpty()) c.setTelefone(telefone);
            if (!linkCurriculo.isEmpty()) c.setLinkCurriculo(linkCurriculo);

            service.salvarCandidato(c);
            JOptionPane.showMessageDialog(this, "Candidato salvo com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}