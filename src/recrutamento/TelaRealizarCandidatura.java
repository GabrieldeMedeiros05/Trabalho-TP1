package recrutamento;

import javax.swing.*;
import java.awt.*;

public class TelaRealizarCandidatura extends JFrame {
    public TelaRealizarCandidatura() {
        setTitle("Candidatura - Associar Candidato à Vaga");
        setSize(520, 300);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("ID do Candidato:"));
        form.add(new JTextField());
        form.add(new JLabel("ID da Vaga:"));
        form.add(new JTextField());
        form.add(new JLabel("Status inicial:"));
        form.add(new JTextField("Pendente"));

        JButton salvar = new JButton("Cadastrar Candidatura (protótipo)");

        setLayout(new BorderLayout(8, 8));
        add(form, BorderLayout.CENTER);
        add(salvar, BorderLayout.SOUTH);
    }
}
