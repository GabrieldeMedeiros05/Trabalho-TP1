package Financeiro.ui;

import Candidatura.dominio.Candidato;
import Candidatura.servico.CandidaturaService;
import Candidatura.ui.*;
import Financeiro.FolhaPagamentoTableModel;
import Financeiro.dados.LeitorFuncionario;
import Seguranca.dominio.Funcionario;
import utils.Constantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaFinanceiro extends JFrame {
    private final CandidaturaService service = CandidaturaModuleConfig.candidaturaService();

    private JTable tabela;
    private JTextField txtFiltroCpf;
    private JTextField txtFiltroNome;

    private JTextField txtFiltroCargo;
    private JTextField txtFiltroTipoContratacao;
    private JTextField txtFiltroStatus;
    private JTextField txtFiltroDepartamento;

    public TelaFinanceiro() {
        setTitle("Financeiro: Funcionários");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // NORTE
//        JPanel botoesEsquerda = new JPanel();
//        JPanel footer = new JPanel(new BorderLayout());
//        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//
//        JButton btnFechar = new JButton("Fechar");
//        JButton btnPdf = new JButton("PDF");
//        JButton btnCsv = new JButton("CSV");
//
//        botoesEsquerda.add(new JLabel("Exportar:"));
//        botoesEsquerda.add(btnPdf);
//        botoesEsquerda.add(btnCsv);
//
//
//        footer.add(botoesEsquerda, BorderLayout.WEST);
//        footer.add(btnFechar, BorderLayout.EAST);
//        add(footer, BorderLayout.SOUTH);
        txtFiltroCpf = new JTextField(9);
        txtFiltroNome = new JTextField(15);
        txtFiltroCargo = new JTextField(9);
        txtFiltroTipoContratacao = new JTextField(5);
        txtFiltroStatus = new JTextField(5);
        txtFiltroDepartamento = new JTextField(10);

        JPanel botoesEsquerda = new JPanel();
        JPanel botoesDireita = new JPanel();
        JPanel painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnBuscar = new JButton("Filtrar");
        JButton btnLimpar = new JButton("Listar Todos");

        botoesEsquerda.add(new JLabel("CPF:"));
        botoesEsquerda.add(txtFiltroCpf);
        botoesEsquerda.add(new JLabel("Nome:"));
        botoesEsquerda.add(txtFiltroNome);
        botoesEsquerda.add(new JLabel("Cargo:"));
        botoesEsquerda.add(txtFiltroCargo);
        botoesEsquerda.add(new JLabel("Regime:"));
        botoesEsquerda.add(txtFiltroTipoContratacao);
        botoesEsquerda.add(new JLabel("Status:"));
        botoesEsquerda.add(txtFiltroStatus);
        botoesEsquerda.add(new JLabel("Departamento:"));
        botoesEsquerda.add(txtFiltroDepartamento);

        botoesDireita.add(btnBuscar, BorderLayout.WEST);
        botoesDireita.add(btnLimpar, BorderLayout.EAST);

        painelBusca.add(botoesEsquerda, BorderLayout.WEST);
        painelBusca.add(botoesDireita, BorderLayout.EAST);
        add(painelBusca, BorderLayout.NORTH);

        // CENTRO
        List<Funcionario> funcionarios = new LeitorFuncionario().retornaListaAtivos();
        FolhaPagamentoTableModel model = new FolhaPagamentoTableModel(funcionarios);

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // SUL
        JButton btnFechar = new JButton("Fechar");
        JButton btnCalcularSalario = new JButton("Calcular Salários");
        JButton btnGerarFolha = new JButton("Gerar Folha");

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(btnGerarFolha);
        footer.add(btnCalcularSalario);
        footer.add(btnFechar);
        add(footer, BorderLayout.SOUTH);

        // Listeners
        btnFechar.addActionListener(e -> dispose());
        btnGerarFolha.addActionListener(e -> abrirFolhaPagamento());

        pack();
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);

    }

    private void abrirFolhaPagamento() {
        TelaFolhaPagamento tela = new TelaFolhaPagamento();
        tela.setVisible(true);
//        this.setVisible(false);
    }

}
