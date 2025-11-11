package Candidatura.ui;

import Candidatura.dominio.Candidato;
import Candidatura.dominio.Candidatura;
import Candidatura.servico.CandidaturaService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Tela simples para associar um candidato (por CPF) a uma vaga (por ID).
 * Usa CandidaturaService.registrarCandidatura.
 */
public class TelaRealizarCandidatura extends JFrame {

    private final CandidaturaService service = CandidaturaModuleConfig.service();

    private JTextField txtCpf;
    private JTextField txtVagaId;

    public TelaRealizarCandidatura() {
        setTitle("Realizar Candidatura");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(2,2,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        form.add(new JLabel("CPF do Candidato:"));
        txtCpf = new JTextField();
        form.add(txtCpf);

        form.add(new JLabel("ID da Vaga:"));
        txtVagaId = new JTextField();
        form.add(txtVagaId);

        JButton btnSalvar = new JButton("Cadastrar Candidatura");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setSize(480, 200);
        setLocationRelativeTo(null);
    }

    private void salvar() {
        String cpf = txtCpf.getText().trim();
        String vaga = txtVagaId.getText().trim();

        if (cpf.isEmpty() || vaga.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha CPF e ID da Vaga.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Optional<Candidato> optCandidato = service.buscarCandidatoPorCpf(cpf);

            if (optCandidato.isEmpty()) {
                int opt = JOptionPane.showConfirmDialog(this, "Candidato não encontrado. Deseja cadastrar agora?", "Candidato não encontrado", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    new TelaCadastroCandidato().setVisible(true);
                    return;
                } else {
                    return;
                }
            }

            Candidatura c = service.registrarCandidatura(vaga, cpf);
            JOptionPane.showMessageDialog(this, "Candidatura registrada (ID: " + c.getId() + ")", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar candidatura: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}