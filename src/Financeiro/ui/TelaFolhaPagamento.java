package Financeiro.ui;

import Candidatura.dominio.Candidato;
import Candidatura.servico.CandidaturaService;
import Candidatura.ui.CandidaturaModuleConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Financeiro.FuncionarioTableModel;
import Seguranca.dominio.Funcionario;
import projeto.Constantes;

public class TelaFolhaPagamento extends JFrame {
    private final CandidaturaService service = CandidaturaModuleConfig.candidaturaService();

    private JTable tabela;
    private DefaultTableModel model;

    public TelaFolhaPagamento() {
        setTitle("Financeiro: Folha de Pagamento");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // --- 1. Painel de Busca (NORTH) ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // --- 2. Tabela (CENTER) ---

        // Dados da tabela
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Francisco da Luz", "Física", "014.873.692-04;1999-01-29", "dataNascimento", "emailPessoal", "telefonePessoal", "enderecoCompleto", "matricula", "cargo", "departamento", "dataContratacao", 1000f, "status", "tipoContrato", 44, "emailCorporativo", "telefoneFixo", "telefoneResidencial", "telefoneCelularCorporativo"));
        funcionarios.add(new Funcionario("Theodoro da Paz", "Física", "420.635.971-70;1979-06-29", "dataNascimento", "emailPessoal", "telefonePessoal", "enderecoCompleto", "matricula", "cargo", "departamento", "dataContratacao", 1000f, "status", "tipoContrato", 44, "emailCorporativo", "telefoneFixo", "telefoneResidencial", "telefoneCelularCorporativo"));
        funcionarios.add(new Funcionario("Valentim Vieira", "Física", "893.725.140-04", "dataNascimento", "emailPessoal", "telefonePessoal", "enderecoCompleto", "matricula", "cargo", "departamento", "dataContratacao", 1000f, "status", "tipoContrato", 44, "emailCorporativo", "telefoneFixo", "telefoneResidencial", "telefoneCelularCorporativo"));
        FuncionarioTableModel model = new FuncionarioTableModel(funcionarios);

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- 3. Rodapé com Botões de Ação (SOUTH) ---
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

        // --- 4. Listeners ---
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