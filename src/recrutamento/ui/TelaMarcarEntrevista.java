package recrutamento.ui;

import javax.swing.*;
import java.awt.*;

public class TelaMarcarEntrevista extends JFrame {
    public TelaMarcarEntrevista() {
        setTitle("Entrevistas - Marcar");
        setSize(520, 320);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("ID Candidatura:"));
        form.add(new JTextField());
        form.add(new JLabel("Data/Hora:"));
        form.add(new JTextField("dd/mm/aaaa hh:mm"));
        form.add(new JLabel("Avaliador:"));
        form.add(new JTextField());
        form.add(new JLabel("Observações:"));
        form.add(new JTextField());

        JButton agendar = new JButton("Agendar (protótipo)");

        setLayout(new BorderLayout(8, 8));
        add(form, BorderLayout.CENTER);
        add(agendar, BorderLayout.SOUTH);
    }
}
