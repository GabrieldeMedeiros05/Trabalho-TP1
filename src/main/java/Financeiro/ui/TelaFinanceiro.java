package Financeiro.ui;

import Candidatura.servico.CandidaturaService;
import Candidatura.ui.*;
import Financeiro.FolhaPagamentoTableModel;
import Financeiro.dados.LeitorFuncionario;
import Seguranca.dominio.Funcionario;
import utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaFinanceiro extends JFrame {

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
        botoesEsquerda = new JPanel();
        botoesDireita = new JPanel();
        painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnFechar = new JButton("Fechar");
        JButton btnCalcularSalario = new JButton("Calcular Salários");
        JButton btnGerarFolha = new JButton("Gerar Folha");

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        botoesEsquerda.add(btnGerarFolha);
        botoesEsquerda.add(btnCalcularSalario);
        botoesDireita.add(btnFechar);

        footer.add(botoesEsquerda, BorderLayout.WEST);
        footer.add(botoesDireita, BorderLayout.EAST);

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
