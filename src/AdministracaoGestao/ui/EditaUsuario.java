package AdministracaoGestao.ui;

import Candidatura.excecoes.RegraNegocioException;
import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import main.AppConfig;

import java.awt.*;
import javax.swing.*;


public class EditaUsuario extends JFrame  {

    private final UsuarioService usuarioService = AppConfig.usuarioService();

    JTextField textNome;
    JTextField textCpf;
    JTextField textLogin;
    JPasswordField textSenha;
    JComboBox<String> cmbTipo;


    public EditaUsuario() {
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
        String cpfCnpj = textCpf.getText().trim();
        String login = textLogin.getText().trim();
        String senha = new String(textSenha.getPassword());
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nome.isEmpty() || cpfCnpj.isEmpty() || login.isEmpty() || senha.isEmpty() || tipo == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Define o departamento baseado no tipo de usuário
            String departamento = tipo.equals("RH") ? "RECRUTAMENTO" : "ADMINISTRACAO";

            // Cria a entidade Usuario (ID = 0L, será gerado na persistência)
            Usuario novoUsuario = new Usuario(
                    0L,
                    login,
                    senha,
                    tipo,
                    nome,
                    cpfCnpj,
                    "ATIVO",
                    departamento
            );

            usuarioService.editarUsuario();

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
        panel.setBounds(0,40,1280,100);

        JLabel title = new JLabel("Editar de Usuário");

        title.setBounds(450,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel = new JPanel(new GridLayout(1, 1, 0, 20));
        panel.setBounds(80, 180, 150, 320);
        panel.setBackground(new Color(10,20,30));

        JLabel labelCpf = new JLabel("CPF/CNPJ");
        labelCpf.setForeground(new Color(240,246,252));
        labelCpf.setFont(new Font("Roboto", Font.PLAIN, 20));

        panel.add(labelCpf);

        return panel;
    }

    public JPanel midPanel2(){

        JPanel panel = new JPanel(new GridLayout(1, 1, 0, 20));
        panel.setBounds(270, 180, 860, 320);
        panel.setBackground(new Color(10,20,30));

        textCpf = new JTextField();
        textCpf.setCaretColor(Color.green);
        textCpf.setBackground(new Color(50,50,50));
        textCpf.setForeground(new Color(240,246,252));
        textCpf.setFont(new Font(null, Font.PLAIN, 20));

        panel.add(textCpf);

        return panel;
    }

    JButton botaoCadastrar;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
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
