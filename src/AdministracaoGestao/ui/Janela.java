
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Janela extends JFrame implements ActionListener {

    public Janela() {
        initComponets();
    }

    public void initComponets(){
        setTitle("Cadastro de Usuário");
        setSize(1280,720);
        setLayout(null);
        setVisible(true);
        
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
        panel.add(midPanel2());
        panel.add(lowerPanel());

        return panel;
    }

    public JPanel upPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(0,0,1280,120);

        JLabel title = new JLabel("Cadastro de Usuário");
        
        title.setBounds(450,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel1 = new JPanel(new GridLayout(4, 1));
        panel1.setBounds(40, 120, 150, 320);
        panel1.setBackground(new Color(10,20,30));

        JLabel label1 = new JLabel("Nome");
        label1.setForeground(new Color(240,246,252));
        label1.setFont(new Font(null, Font.PLAIN, 20));

        JLabel label2 = new JLabel("CPF/CNPJ");
        label2.setForeground(new Color(240,246,252));
        label2.setFont(new Font(null, Font.PLAIN, 20));

        JLabel label3 = new JLabel("Status");
        label3.setForeground(new Color(240,246,252));
        label3.setFont(new Font(null, Font.PLAIN, 20));

        JLabel label4 = new JLabel("Departamento");
        label4.setForeground(new Color(240,246,252));
        label4.setFont(new Font(null, Font.PLAIN, 20));

        panel1.add(label1);
        panel1.add(label2);
        panel1.add(label3);
        panel1.add(label4);

        return panel1;
    }

    JTextField text1;
    JTextField text2;
    JTextField text3;
    JTextField text4;

    public JPanel midPanel2(){

        JPanel panel2 = new JPanel(new GridLayout(4, 1, 0, 10));
        panel2.setBounds(250, 120, 860, 320);
        panel2.setBackground(new Color(10,20,30));
        

        text1 = new JTextField();
        text1.setCaretColor(Color.green);
        text1.setBackground(new Color(50,50,50));
        text1.setForeground(new Color(240,246,252));
        text1.setFont(new Font(null, Font.PLAIN, 20));
        text1.setText("Fulano da Silva");

        text2 = new JTextField();
        text2.setCaretColor(Color.green);
        text2.setBackground(new Color(50,50,50));
        text2.setForeground(new Color(240,246,252));
        text2.setFont(new Font(null, Font.PLAIN, 20));
        text2.setText("XXX.XXX.XXX-XX");

        text3 = new JTextField();
        text3.setCaretColor(Color.green);
        text3.setBackground(new Color(50,50,50));
        text3.setForeground(new Color(240,246,252));
        text3.setFont(new Font(null, Font.PLAIN, 20));
        text3.setText("Ativo");

        text4 = new JTextField();
        text4.setCaretColor(Color.green);
        text4.setBackground(new Color(50,50,50));
        text4.setForeground(new Color(240,246,252));
        text4.setFont(new Font(null, Font.PLAIN, 20));
        text4.setText("Recrutamento");

        panel2.add(text1);
        panel2.add(text2);
        panel2.add(text3);
        panel2.add(text4);
    
        return panel2;
    }

    JButton botao;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(300,550,1280,120);

        botao = new JButton("Cadastrar");
        
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
            
            this.dispose();
        }


    }

    public static void main(String[] args) {

        new Janela().setVisible(true);
        
    }


    

}