package recrutamento.ui;

import javax.swing.*;
import java.awt.*;

public class TelaSolicitarContratacao extends JFrame {
    public TelaSolicitarContratacao() {
        setTitle("Contratação - Solicitar");
        setSize(520, 320);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("ID Candidato Aprovado:"));
        form.add(new JTextField());
        form.add(new JLabel("ID da Vaga:"));
        form.add(new JTextField());
        form.add(new JLabel("Regime (CLT/Estágio/PJ):"));
        form.add(new JTextField());
        form.add(new JLabel("Data de Contratação:"));
        form.add(new JTextField("dd/mm/aaaa"));

        JButton solicitar = new JButton("Solicitar (protótipo)");

        setLayout(new BorderLayout(8, 8));
        add(form, BorderLayout.CENTER);
        add(solicitar, BorderLayout.SOUTH);
    }
}
