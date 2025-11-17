package recrutamento.ui;

import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import java.awt.*;

public class TelaConsultarContratacoes extends JFrame {
    public TelaConsultarContratacoes(RecrutamentoService service) {
        setTitle("Contratações - Consultar");
        setSize(740, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel filtros = new JPanel(new GridLayout(0, 8, 6, 6));
        filtros.add(new JLabel("Vaga:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Candidato:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Regime:"));
        filtros.add(new JTextField());
        filtros.add(new JLabel("Status:"));
        filtros.add(new JTextField("Solicitada/Autorizada/Negada/Concluída"));

        JButton buscar = new JButton("Buscar");
        JButton detalhar = new JButton("Detalhar (protótipo)");

        String[] colunas = {"ID", "Vaga", "Candidato", "Regime", "Status", "Data"};
        JTable tabela = new JTable(new Object[][]{}, colunas);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(filtros, BorderLayout.CENTER);
        topo.add(buscar, BorderLayout.EAST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(detalhar);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(acoes, BorderLayout.SOUTH);
    }

}