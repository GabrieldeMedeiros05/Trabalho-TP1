// package AdministracaoGestao.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AdministracaoGestaoDeUsuario extends JFrame implements ActionListener {

    public AdministracaoGestaoDeUsuario() {
        initComponets();
    }

    public void initComponets(){
        setTitle("Administração/Gestão de Usuário");
        setSize(1280,720);
        setLayout(null);
        
        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);

        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainPanel());
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
        panel.setBounds(0,0,1280,120);

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

        panel.setLayout(new GridLayout(2,3,150,250));

        panel.setBounds(90, 150, 1100, 320);
        panel.setBackground(new Color(10,20,30));

        JPanel panel1 = new JPanel(new GridLayout(1, 1));

        botao1 = new JButton("Cadastrar");
        botao1.setBackground(new Color(21,27,35));
        botao1.setForeground(new Color(240,246,252));
        botao1.setFocusable(false);
        botao1.addActionListener(this);

        panel1.add(botao1);

        panel.add(panel1);

        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        

        botao2 = new JButton("Editar");
        botao2.setBackground(new Color(21,27,35));
        botao2.setForeground(new Color(240,246,252));
        botao2.setFocusable(false);
        botao2.addActionListener(this);

        panel2.add(botao2);
    
        panel.add(panel2);

        JPanel panel3 = new JPanel(new GridLayout(1, 1));

        botao3 = new JButton("Excluir");
        botao3.setBackground(new Color(21,27,35));
        botao3.setForeground(new Color(240,246,252));
        botao3.setFocusable(false);
        botao3.addActionListener(this);

        panel3.add(botao3);
    
        panel.add(panel3);

        JPanel panel4 = new JPanel(new GridLayout(1, 1));

        botao4 = new JButton("Listar");
        botao4.setBackground(new Color(21,27,35));
        botao4.setForeground(new Color(240,246,252));
        botao4.setFocusable(false);
        botao4.addActionListener(this);

        panel4.add(botao4);
    
        panel.add(panel4);

        JPanel panel5 = new JPanel(new GridLayout(1, 1));

        botao5 = new JButton("Pesquisar");
        botao5.setBackground(new Color(21,27,35));
        botao5.setForeground(new Color(240,246,252));
        botao5.setFocusable(false);
        botao5.addActionListener(this);
        
        panel5.add(botao5);
    
        panel.add(panel5);

        JPanel panel6 = new JPanel(new GridLayout(1, 1));

        botao6 = new JButton("Validar");
        botao6.setBackground(new Color(21,27,35));
        botao6.setForeground(new Color(240,246,252));
        botao6.setFocusable(false);
        botao6.addActionListener(this);

        panel6.add(botao6);
    
        panel.add(panel6);

        return panel;
    }

    JButton botao;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(300,550,1280,120);

        botao = new JButton("Gerar Relatório");
        
        botao.setBounds(250,30,200,45);
        botao.setBackground(new Color(21,27,35));
        botao.setForeground(new Color(240,246,252));
        botao.setFocusable(false);
        botao.addActionListener(this);

        panel.add(botao);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==botao) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }

        if (e.getSource()==botao1) {
            
            CadastroUsuario cadastro = new CadastroUsuario();
        }

        if (e.getSource()==botao2) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }

        if (e.getSource()==botao3) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }

        if (e.getSource()==botao4) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }

        if (e.getSource()==botao5) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }

        if (e.getSource()==botao6) {
            
            CadastroUsuario janela = new CadastroUsuario();
        }


    }

    public static void main(String[] args) {

        new AdministracaoGestaoDeUsuario().setVisible(true);
        
    }

    
}