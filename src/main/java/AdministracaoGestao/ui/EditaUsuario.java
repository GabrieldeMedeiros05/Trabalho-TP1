package AdministracaoGestao.ui;

import Candidatura.excecoes.RegraNegocioException;
import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import utils.AppConfig;

import java.awt.*;
import javax.swing.*;
import java.util.Optional;

import static utils.Constantes.COR_DE_FUNDO;


public class EditaUsuario extends JFrame  {

    private final UsuarioService usuarioService = AppConfig.usuarioService();

    JTextField textNome;
    JTextField textCpf;
    JTextField textLogin;
    JTextField textSenha;
    JComboBox<String> cmbTipo;
    Optional<Usuario> usuario;

    public EditaUsuario(Optional<Usuario> usuario) {

        this.usuario = usuario;

        initComponets();
    }

    public void initComponets(){
        setTitle("Editar de Usuário");
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

    private void editarUsuario() {
        String nome = textNome.getText().trim();
        String login = textLogin.getText().trim();
        String senha = new String(textSenha.getText().trim());
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nome.isEmpty() & login.isEmpty() & senha.isEmpty() & tipo.equals(usuario.get().getTipo())) {
            JOptionPane.showMessageDialog(this, "Nenhum campo alterado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            // Define o departamento baseado no tipo de usuário
            String departamento = tipo.equals("RH") ? "RECRUTAMENTO" : "ADMINISTRACAO";

            usuarioService.editarUsuario(nome, login, senha, tipo, usuario);

            JOptionPane.showMessageDialog(this,
                    "Usuário " + tipo + " editado com sucesso! Login: " + login,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
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

        JLabel title = new JLabel("Editar de Usuário");

        title.setBounds(450,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 20));
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
        panel.add(labelLogin);
        panel.add(labelSenha);
        panel.add(labelTipo);

        return panel;
    }

    public JPanel midPanel2(){

        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 20));
        panel.setBounds(270, 180, 860, 320);
        panel.setBackground(COR_DE_FUNDO);

        textNome = new JTextField(usuario.get().getNome());
        textNome.setCaretColor(Color.green);
        textNome.setBackground(new Color(50,50,50));
        textNome.setForeground(new Color(240,246,252));
        textNome.setFont(new Font(null, Font.PLAIN, 20));

        textLogin = new JTextField(usuario.get().getLogin());
        textLogin.setCaretColor(Color.green);
        textLogin.setBackground(new Color(50,50,50));
        textLogin.setForeground(new Color(240,246,252));
        textLogin.setFont(new Font(null, Font.PLAIN, 20));

        textSenha = new JTextField(usuario.get().getSenha());
        textSenha.setCaretColor(Color.green);
        textSenha.setBackground(new Color(50,50,50));
        textSenha.setForeground(new Color(240,246,252));
        textSenha.setFont(new Font(null, Font.PLAIN, 20));

        cmbTipo = new JComboBox<>(new String[]{"Administrador", "Gestor", "Recrutador", "Funcionario", "Candidato"});
        cmbTipo.setBackground(new Color(50,50,50));
        cmbTipo.setForeground(new Color(240,246,252));
        cmbTipo.setFont(new Font(null, Font.PLAIN, 20));

        panel.add(textNome);
        panel.add(textLogin);
        panel.add(textSenha);
        panel.add(cmbTipo);

        return panel;
    }

    JButton botaoCadastrar;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(300,540,1280,120);

        botaoCadastrar = new JButton("Editar");

        botaoCadastrar.setBounds(250,30,200,45);
        botaoCadastrar.setBackground(new Color(21,27,35));
        botaoCadastrar.setForeground(new Color(240,246,252));
        botaoCadastrar.setFocusable(false);
        //botao.addActionListener(this);
        botaoCadastrar.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botaoCadastrar);

        botaoCadastrar.addActionListener(e -> editarUsuario());

        return panel;
    }

}
