package Financeiro.ui;

import Candidatura.servico.CandidaturaService;
import Candidatura.ui.CandidaturaModuleConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import Financeiro.FolhaPagamentoTableModel;
import Financeiro.dados.LeitorFuncionario;
import Seguranca.dominio.Funcionario;
import utils.Constantes;

public class TelaFolhaPagamento extends JFrame {
    private final CandidaturaService service = CandidaturaModuleConfig.candidaturaService();

    private JTable tabela;
    private DefaultTableModel model;

    public TelaFolhaPagamento() {
        setTitle("Financeiro: Folha de Pagamento");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // NORTE
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // CENTER
        // Dados da tabela
        List<Funcionario> funcionarios = new LeitorFuncionario().retornaListaAtivos();
        FolhaPagamentoTableModel model = new FolhaPagamentoTableModel(funcionarios);

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // SUL
        JPanel botoesEsquerda = new JPanel();
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnFechar = new JButton("Fechar");
        JButton btnPdf = new JButton("PDF");
        JButton btnCsv = new JButton("CSV");

        botoesEsquerda.add(new JLabel("Exportar:"));
        botoesEsquerda.add(btnPdf);
        botoesEsquerda.add(btnCsv);


        footer.add(botoesEsquerda, BorderLayout.WEST);
        footer.add(btnFechar, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        // Listeners
        btnFechar.addActionListener(e -> dispose());
//        btnGerarFolha.addActionListener(e -> abrirRelatroioFinanceiro());

        pack();
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);

    }

    private void abrirRelatroioFinanceiro() {
        TelaFinanceiro tela = new TelaFinanceiro();
        tela.setVisible(true);
        this.setVisible(false);
    }

}