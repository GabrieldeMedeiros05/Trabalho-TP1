package recrutamento.ui;

import javax.swing.*;
import java.awt.*;

public class TelaListarCandidatos extends JFrame {
    public TelaListarCandidatos() {
        setTitle("Candidatos - Listar/Buscar");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel filtros = new JPanel(new GridLayout(0, 6, 6, 6));
        filtros.add(new JLabel("Nome:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("CPF:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Formação:"));
        filtros.add(new JTextField());

        JButton buscar = new JButton("Buscar");
        JButton abrir = new JButton("Abrir (protótipo)");
        JButton candidatar = new JButton("Candidatar (protótipo)");

        String[] colunas = {"ID", "Nome", "CPF", "Formação", "Experiência"};
        JTable tabela = new JTable(new Object[][]{}, colunas);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(filtros, BorderLayout.CENTER);
        topo.add(buscar, BorderLayout.EAST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(abrir);
        acoes.add(candidatar);

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(acoes, BorderLayout.SOUTH);
    }
}
