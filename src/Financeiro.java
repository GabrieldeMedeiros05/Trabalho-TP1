import javax.swing.*;

public class Financeiro extends JFrame {
    private JPasswordField passwordField1;
    private JCheckBox checkBox1;
    private JPanel PainelFinanceiro;

    public Financeiro() {
        setContentPane(PainelFinanceiro);
        setTitle("Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        new Financeiro();
    }
}
