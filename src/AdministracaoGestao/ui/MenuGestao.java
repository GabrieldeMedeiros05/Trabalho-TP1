package AdministracaoGestao.ui;

// import Gestao.ui.CadastroUsuario;
// import Gestao.ui.ExcluiUsuario;

import java.awt.*;
import javax.swing.*;

public class MenuGestao extends JFrame {

    public MenuGestao() {
        initComponets();
    }

    public void initComponets(){
        setVisible(true);
        setTitle("Administração/Gestão de Usuário");
        setSize(1280,720);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);

        setLocationRelativeTo(null);

        add(mainPanel());

        configurarListeners();
    }

    public JPanel mainPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));

        panel.add(upPanel());
        panel.add(midPanel());
        panel.add(lowerPanel());

        return panel;
    }

    public JPanel upPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(20,40,1280,100);

        JLabel title = new JLabel("Administração/Gestão de Usuário");
        
        title.setBounds(280,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    JButton botao1;
    JButton botao2;
    JButton botao3;
    JButton botao4;
    JButton botao5;
    JButton botao6;

    public JPanel midPanel(){

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(2,3,150,110));

        panel.setBounds(100, 220, 1100, 220);
        panel.setBackground(new Color(10,20,30));

        JPanel panel1 = new JPanel(new GridLayout(1, 1));

        botao1 = new JButton("Cadastrar");
        botao1.setBackground(new Color(21,27,35));
        botao1.setForeground(new Color(240,246,252));
        botao1.setFocusable(false);
        botao1.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel1.add(botao1);

        panel.add(panel1);

        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        

        botao2 = new JButton("Editar");
        botao2.setBackground(new Color(21,27,35));
        botao2.setForeground(new Color(240,246,252));
        botao2.setFocusable(false);
        botao2.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel2.add(botao2);
    
        panel.add(panel2);

        JPanel panel3 = new JPanel(new GridLayout(1, 1));

        botao3 = new JButton("Excluir");
        botao3.setBackground(new Color(21,27,35));
        botao3.setForeground(new Color(240,246,252));
        botao3.setFocusable(false);
        botao3.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel3.add(botao3);
    
        panel.add(panel3);

        JPanel panel4 = new JPanel(new GridLayout(1, 1));

        botao4 = new JButton("Listar");
        botao4.setBackground(new Color(21,27,35));
        botao4.setForeground(new Color(240,246,252));
        botao4.setFocusable(false);
        botao4.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel4.add(botao4);
    
        panel.add(panel4);

        JPanel panel5 = new JPanel(new GridLayout(1, 1));

        botao5 = new JButton("Pesquisar");
        botao5.setBackground(new Color(21,27,35));
        botao5.setForeground(new Color(240,246,252));
        botao5.setFocusable(false);
        botao5.setFont(new Font("Roboto", Font.PLAIN, 17));
        
        panel5.add(botao5);
    
        panel.add(panel5);

        JPanel panel6 = new JPanel(new GridLayout(1, 1));

        botao6 = new JButton("Validar");
        botao6.setBackground(new Color(21,27,35));
        botao6.setForeground(new Color(240,246,252));
        botao6.setFocusable(false);
        botao6.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel6.add(botao6);
    
        panel.add(panel6);

        return panel;
    }

    JButton botao;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(300,540,1280,120);

        botao = new JButton("Gerar Relatório");
        
        botao.setBounds(250,30,200,45);
        botao.setBackground(new Color(21,27,35));
        botao.setForeground(new Color(240,246,252));
        botao.setFocusable(false);
        botao.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botao);

        return panel;
    }

    private void abrir(JFrame f) {
        f.setLocationRelativeTo(this);
        f.setVisible(true);
    }

    private void configurarListeners() {
        botao1.addActionListener(e -> abrir(new CadastroUsuario()));
        botao3.addActionListener(e -> abrir(new ExcluiUsuario()));
        
    }


    // public void configurarListeners() {
    //     botao3.addActionListener(e -> abrirModuloGestao());
    //     //botao3.addActionListener(e -> abrir(new ExcluiUsuario()));
    //     // sairButton.addActionListener(e -> dispose());
    // }

    // private void abrirModuloGestao() {
    //     Outrinho tela = new Outrinho();
    //     tela.setVisible(true);
    // }

    // public static void main(String[] args) {

    //     new MenuGestao().setVisible(true);
        
    // }

    
}