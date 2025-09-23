import javax.swing.*;

public class Financeiro extends JFrame {
    public Financeiro() {
        setTitle("Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    private JTextField textField1;
    private JFormattedTextField formattedTextField1;

    public static void main(String[] args) {
        new Financeiro();
    }
}
