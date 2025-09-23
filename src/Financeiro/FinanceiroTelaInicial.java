package Financeiro;

import javax.swing.*;
import projeto.Constantes;

class FinanceiroTelaInicial extends JFrame {
    private JPanel PainelFinanceiro;
    private JButton cadastrarRegrasSalariaisButton;
    private JButton calcularSalárioButton;
    private JButton visualizarRegrasSalariaisButton;
    private JButton gerarFolhaDePagamentoButton;
    private JButton listarFuncionáriosButton;

    public FinanceiroTelaInicial() {
        setContentPane(PainelFinanceiro);
        setTitle("Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}