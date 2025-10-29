package Financeiro;
import javax.swing.*;
import projeto.Constantes;

public class VisualizaRegra extends JFrame {
    private JPanel PainelVisualizaRegra;
    private JLabel visualizarRegrasSalariaisLabel;
    private JTable tabelaDeRegras;
    private JButton voltarButton;


    public VisualizaRegra() {
        setContentPane(PainelVisualizaRegra);
        setTitle("Visualizar de Regras Salariais");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createUIComponents() {
        String data[][] = {
                {"1", "Regra1", "Descrição da Regra1", "Fixo", "R$300"},
                {"2", "Regra2", "Descrição da Regra2", "Porcentagem", "20%"},
                {"3", "Regra3", "Descrição da Regra3", "Fixo", "R$400"},
                {"4", "Regra4", "Descrição da Regra4", "Porcentagem", "20%"},
                {"5", "Regra5", "Descrição da Regra5", "Fixo", "R$500"},
                {"6", "Regra6", "Descrição da Regra6", "Porcentagem", "20%"},
        };
        String Column[] = {"id", "nome", "descrição", "Natureza", "Valor"};

        tabelaDeRegras = new JTable(data, Column);
        tabelaDeRegras.setCellSelectionEnabled(true);
    }
}
