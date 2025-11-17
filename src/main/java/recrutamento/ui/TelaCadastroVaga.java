package recrutamento.ui;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroVaga extends JFrame {
    public TelaCadastroVaga() {
        setTitle("Vagas - Cadastro/Edição");
        setSize(520, 480);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Cargo:"));
        form.add(new JTextField());
        form.add(new JLabel("Departamento:"));
        form.add(new JTextField());
        form.add(new JLabel("Salário Base:"));
        form.add(new JTextField());
        form.add(new JLabel("Regime (CLT/Estágio/PJ):"));
        form.add(new JTextField());
        form.add(new JLabel("Data de Abertura:"));
        form.add(new JTextField("dd/mm/aaaa"));
        form.add(new JLabel("Requisitos:"));
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
