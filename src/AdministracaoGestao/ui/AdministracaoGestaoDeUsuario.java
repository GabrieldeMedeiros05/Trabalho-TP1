package AdministracaoGestao.ui;

import javax.swing.*;
import projeto.Constantes;

class AdministracaoGestaoDeUsuario extends JFrame {
    private JPanel PainelUsuario;
    private JButton excluirButton;
    private JButton listarButton;
    private JButton editarButton;

    public AdministracaoGestaoDeUsuario() {
        setContentPane(PainelUsuario);
        setTitle("Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}