package Financeiro;
import javax.swing.*;
import projeto.Constantes;

public class CadastroRegra extends JFrame {
    private JPanel PainelCadastroRegra;
    private JLabel lbCadastramentoDeRegras;
    private JTextField textField1;
    private JTextArea textArea1;
    private JRadioButton valorFixoRadioButton;
    private JRadioButton porcentagemDoSalárioRadioButton;
    private JTextField textField2;
    private JButton voltarButton;

    public CadastroRegra() {
        setContentPane(PainelCadastroRegra);
        setTitle("Cadastro de Regras Salariais");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
