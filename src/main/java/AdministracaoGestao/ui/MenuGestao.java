package AdministracaoGestao.ui;

import java.awt.*;
import javax.swing.*;
import static utils.Constantes.COR_DE_FUNDO;

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
        panel.setBackground(COR_DE_FUNDO);

        panel.add(upPanel());
        panel.add(midPanel());
        panel.add(lowerPanel());

        return panel;
    }

    public JPanel upPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(20,40,1280,100);

        JLabel title = new JLabel("Administração/Gestão de Usuário");
        
        title.setBounds(280,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    JButton botaoCadastrar;
    JButton botaoEditar;
    JButton botaoExcluir;
    JButton botaoListar;
    JButton botaoPesquisar;
    JButton botao6;

    public JPanel midPanel(){

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(2,3,150,110));

        panel.setBounds(100, 220, 1100, 220);
        panel.setBackground(COR_DE_FUNDO);

        JPanel panel1 = new JPanel(new GridLayout(1, 1));

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBackground(new Color(21,27,35));
        botaoCadastrar.setForeground(new Color(240,246,252));
        botaoCadastrar.setFocusable(false);
        botaoCadastrar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel1.add(botaoCadastrar);

        panel.add(panel1);

        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        

        botaoEditar = new JButton("Editar");
        botaoEditar.setBackground(new Color(21,27,35));
        botaoEditar.setForeground(new Color(240,246,252));
        botaoEditar.setFocusable(false);
        botaoEditar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel2.add(botaoEditar);
    
        panel.add(panel2);

        JPanel panel3 = new JPanel(new GridLayout(1, 1));

        botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBackground(new Color(21,27,35));
        botaoExcluir.setForeground(new Color(240,246,252));
        botaoExcluir.setFocusable(false);
        botaoExcluir.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel3.add(botaoExcluir);
    
        panel.add(panel3);

        JPanel panel4 = new JPanel(new GridLayout(1, 1));

        botaoListar = new JButton("Listar");
        botaoListar.setBackground(new Color(21,27,35));
        botaoListar.setForeground(new Color(240,246,252));
        botaoListar.setFocusable(false);
        botaoListar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel4.add(botaoListar);
    
        panel.add(panel4);

        JPanel panel5 = new JPanel(new GridLayout(1, 1));

        botaoPesquisar = new JButton("Pesquisar");
        botaoPesquisar.setBackground(new Color(21,27,35));
        botaoPesquisar.setForeground(new Color(240,246,252));
        botaoPesquisar.setFocusable(false);
        botaoPesquisar.setFont(new Font("Roboto", Font.PLAIN, 17));
        
        panel5.add(botaoPesquisar);
    
        panel.add(panel5);

        return panel;
    }

    JButton botaoFechar;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(300,540,1280,120);

        botaoFechar = new JButton("Fechar");
        
        botaoFechar.setBounds(250,30,200,45);
        botaoFechar.setBackground(new Color(21,27,35));
        botaoFechar.setForeground(new Color(240,246,252));
        botaoFechar.setFocusable(false);
        botaoFechar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botaoFechar);

        return panel;
    }

    private void abrir(JFrame f) {
        f.setLocationRelativeTo(this);
        f.setVisible(true);
    }

    private void configurarListeners() {
        botaoCadastrar.addActionListener(e -> abrir(new CadastroUsuario()));
        botaoEditar.addActionListener(e -> abrir(new ValidarCpfEditar()));
        botaoExcluir.addActionListener(e -> abrir(new ExcluiUsuario()));
	    botaoListar.addActionListener(e -> abrir(new ListaUsuario()));
        botaoPesquisar.addActionListener(e -> abrir(new ValidarCpfPesquisar()));
        botaoFechar.addActionListener(e -> dispose());
        
    }

}
