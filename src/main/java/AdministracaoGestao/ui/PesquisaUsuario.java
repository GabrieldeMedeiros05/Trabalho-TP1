package AdministracaoGestao.ui;

import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import utils.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static utils.Constantes.COR_DE_FUNDO;

public class PesquisaUsuario extends JFrame {

    private final UsuarioService usuarioService = AppConfig.usuarioService();

    JLabel pesquisaNome;
    JLabel pesquisaCpf;
    JLabel pesquisaLogin;
    JLabel pesquisaSenha;
    JLabel pesquisaTipo;
    Optional<Usuario> usuario;

    public PesquisaUsuario(Optional<Usuario> usuario) {

        this.usuario = usuario;

        initComponets();
    }

    public void initComponets(){
        setTitle("Usuário Encontrado");
        setSize(1280,720);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);

        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainPanel());
    }

    private void fecharUsuario() {

        dispose();
    }

    public JPanel mainPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);

        panel.add(upPanel());
        panel.add(midPanel());
        panel.add(midPanel2());
        panel.add(lowerPanel());

        return panel;
    }

    public JPanel upPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(0,40,1280,100);

        JLabel title = new JLabel("Usuário Encontrado");

        title.setBounds(450,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 20));
        panel.setBounds(80, 180, 150, 320);
        panel.setBackground(COR_DE_FUNDO);

        JLabel labelNome = new JLabel("Nome");
        labelNome.setForeground(new Color(240,246,252));
        labelNome.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel labelCpf = new JLabel("CPF/CNPJ");
        labelCpf.setForeground(new Color(240,246,252));
        labelCpf.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel labelLogin = new JLabel("Login");
        labelLogin.setForeground(new Color(240,246,252));
        labelLogin.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel labelSenha = new JLabel("Senha");
        labelSenha.setForeground(new Color(240,246,252));
        labelSenha.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel labelTipo = new JLabel("Tipo");
        labelTipo.setForeground(new Color(240,246,252));
        labelTipo.setFont(new Font("Roboto", Font.PLAIN, 20));

        panel.add(labelNome);
        panel.add(labelCpf);
        panel.add(labelLogin);
        panel.add(labelSenha);
        panel.add(labelTipo);

        return panel;
    }

    public JPanel midPanel2(){

        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 20));
        panel.setBounds(270, 180, 860, 320);
        panel.setBackground(COR_DE_FUNDO);

        pesquisaNome = new JLabel(usuario.get().getNome());
        pesquisaNome.setBackground(new Color(50,50,50));
        pesquisaNome.setForeground(new Color(240,246,252));
        pesquisaNome.setFont(new Font(null, Font.PLAIN, 20));

        pesquisaCpf = new JLabel(usuario.get().getCpf_cnpj());
        pesquisaCpf.setBackground(new Color(50,50,50));
        pesquisaCpf.setForeground(new Color(240,246,252));
        pesquisaCpf.setFont(new Font(null, Font.PLAIN, 20));

        pesquisaLogin = new JLabel(usuario.get().getLogin());
        pesquisaLogin.setBackground(new Color(50,50,50));
        pesquisaLogin.setForeground(new Color(240,246,252));
        pesquisaLogin.setFont(new Font(null, Font.PLAIN, 20));

        pesquisaSenha = new JLabel(usuario.get().getSenha());
        pesquisaSenha.setBackground(new Color(50,50,50));
        pesquisaSenha.setForeground(new Color(240,246,252));
        pesquisaSenha.setFont(new Font(null, Font.PLAIN, 20));

        pesquisaTipo = new JLabel(usuario.get().getTipo());
        pesquisaTipo.setBackground(new Color(50,50,50));
        pesquisaTipo.setForeground(new Color(240,246,252));
        pesquisaTipo.setFont(new Font(null, Font.PLAIN, 20));

        panel.add(pesquisaNome);
        panel.add(pesquisaCpf);
        panel.add(pesquisaLogin);
        panel.add(pesquisaSenha);
        panel.add(pesquisaTipo);

        return panel;
    }

    JButton botaoCadastrar;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(300,540,1280,120);

        botaoCadastrar = new JButton("Fecharr");

        botaoCadastrar.setBounds(250,30,200,45);
        botaoCadastrar.setBackground(new Color(21,27,35));
        botaoCadastrar.setForeground(new Color(240,246,252));
        botaoCadastrar.setFocusable(false);
        //botao.addActionListener(this);
        botaoCadastrar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botaoCadastrar);

        botaoCadastrar.addActionListener(e -> fecharUsuario());

        return panel;
    }
}
