package recrutamento;

import javax.swing.SwingUtilities;

public class MainRecrutamento {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaMenuRecrutamento().setVisible(true));
    }
}
