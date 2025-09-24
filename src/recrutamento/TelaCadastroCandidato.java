package recrutamento;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroCandidato extends JFrame {
    public TelaCadastroCandidato() {
        setTitle("Candidatos - Cadastro");
        setSize(520, 520);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nome:"));
        form.add(new JTextField());
        form.add(new JLabel("CPF:"));
        form.add(new JTextField());
        form.add(new JLabel("Formação:"));
        form.add(new JTextField());
        form.add(new JLabel("Experiência:"));
        form.add(new JTextField());
        form.add(new JLabel("Pretensão Salarial:"));
        form.add(new JTextField());
        form.add(new JLabel("Disponibilidade:"));
        form.add(new JTextField());

        JButton salvar = new JButton("Salvar (protótipo)");
        JButton limpar = new JButton("Limpar");
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(limpar);
        botoes.add(salvar);

        setLayout(new BorderLayout(8, 8));
        add(form, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
    }
}
