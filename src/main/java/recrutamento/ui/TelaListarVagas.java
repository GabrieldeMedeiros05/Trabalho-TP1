package recrutamento.ui;

import javax.swing.*;
import java.awt.*;

public class TelaListarVagas extends JFrame {
    public TelaListarVagas() {
        setTitle("Vagas - Listar/Filtrar");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel filtros = new JPanel(new GridLayout(0, 6, 6, 6));
        filtros.add(new JLabel("Cargo:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Departamento:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Status (aberta/fechada):"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Regime:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Faixa Salarial:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Data Abertura:"));
        filtros.add(new JTextField("mm/aaaa"));

        JButton buscar = new JButton("Aplicar Filtros");
        JButton abrir = new JButton("Abrir (protótipo)");
        JButton editar = new JButton("Editar (protótipo)");
        JButton excluir = new JButton("Excluir (protótipo)");

        String[] colunas = {"ID", "Cargo", "Depto", "Regime", "Salário Base", "Status"};
        JTable tabela = new JTable(new Object[][]{}, colunas);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(filtros, BorderLayout.CENTER);
        topo.add(buscar, BorderLayout.EAST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(abrir);
        acoes.add(editar);
        acoes.add(excluir);

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(acoes, BorderLayout.SOUTH);
    }
}
