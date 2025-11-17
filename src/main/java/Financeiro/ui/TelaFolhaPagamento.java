package Financeiro.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Financeiro.FolhaPagamentoTableModel;
import Financeiro.dados.LeitorFuncionario;
import Seguranca.dominio.Funcionario;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import utils.Constantes;
import utils.PdfWriter;

public class TelaFolhaPagamento extends JFrame {
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
        btnPdf.addActionListener(e -> exportarPdf(funcionarios));
        btnCsv.addActionListener(e -> exportarCsv(funcionarios));

        pack();
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);

    }

    private void abrirRelatroioFinanceiro() {
        TelaFinanceiro tela = new TelaFinanceiro();
        tela.setVisible(true);
        this.setVisible(false);
    }

    private List<List<String>> funcionariosFolhaToString(List<Funcionario> funcionarios) {
        List<List<String>> funcionariosToString = new ArrayList<>();
        funcionariosToString.add(List.of("Matrícula", "Nome", "Cargo", "Departamento", "Status", "Tipo", "Salário Líquido", "Impostos Retidos"));
        for (Funcionario funcionario : funcionarios) {
            List<String> linhaFuncionario = new ArrayList<String>();
            linhaFuncionario.add(funcionario.getMatricula());
            linhaFuncionario.add(funcionario.getNome());
            linhaFuncionario.add(funcionario.getCargo());
            linhaFuncionario.add(funcionario.getDepartamento());
            linhaFuncionario.add(funcionario.getStatus());
            linhaFuncionario.add(funcionario.getTipoContrato());
            linhaFuncionario.add(String.format("R$ %.2f", funcionario.calculaSalarioLiquido()));
            linhaFuncionario.add(String.format("R$ %.2f", funcionario.getImposto()));
            funcionariosToString.add(linhaFuncionario);
        }
        return funcionariosToString;
    }
    private void exportarPdf(List<Funcionario> funcionarios) {
        List<List<String>> funcionariosToString = funcionariosFolhaToString(funcionarios);
        float[] larguraColunas = {0.15f, 0.25f, 0.25f, 0.2f, 0.08f, 0.13f, 0.18f, 0.18f, 0.2f};
        String titulo = "FOLHA DE PAGAMENTO";
        String subtitulo = "Relatório de pagamento para o Mês";

        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage();

            PDRectangle customSize = new PDRectangle(800, 2000);
            pagina.setMediaBox(customSize);

            documento.addPage(pagina);

            float tituloY = 1900;
            float subtituloY = tituloY-50;

            try (PDPageContentStream contentStream = new PDPageContentStream(documento, pagina)) {

                PdfWriter.escreveTextoCentralizado(
                        contentStream, pagina, titulo,
                        tituloY);
                PdfWriter.escreveTexto(
                        contentStream, subtitulo,
                        subtituloY, 35);
                subtituloY -= 20;

                PdfWriter.escreveTabela(funcionariosToString, contentStream, subtituloY, larguraColunas);
            }

            documento.save("folha_de_pagamento_mensal.pdf");

            JFrame framePrincipal = new JFrame("Aviso de Exportação");
            JOptionPane.showMessageDialog(
                    framePrincipal,
                    "Os dados foram salvos em pdf",
                    "Confirmação de exportação",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportarCsv(List<Funcionario> dados) {
        List<List<String>> funcionariosToString = funcionariosFolhaToString(dados);

        List<String> cabecalho = List.of(
                "MATRICULA", "NOME", "CARGO", "DEPARTAMENTO",
                "STATUS", "TIPO", "SALARIO_LIQUIDO",
                "IMPOSTOS_RETIDOS"
        );
        try (FileWriter saida = new FileWriter("folha_de_pagamento_mensal.csv");

             CSVPrinter printer = new CSVPrinter(saida, CSVFormat.DEFAULT.withHeader(cabecalho.toArray(String[]::new)))) {

            for (int i = 1; i < funcionariosToString.size(); i++) {
                List<String> registro = funcionariosToString.get(i);

                // Imprime a lista interna como um registro (linha) no CSV
                printer.printRecord(registro);
            }

            JFrame framePrincipal = new JFrame("Aviso de Exportação");
            JOptionPane.showMessageDialog(
                    framePrincipal,
                    "Os dados foram salvos em csv",
                    "Confirmação de exportação",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

}