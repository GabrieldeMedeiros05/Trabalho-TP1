package AdministracaoGestao.ui;

import Candidatura.excecoes.RegraNegocioException;
import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import main.AppConfig;

import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import javax.swing.*;

public class CadastroUsuario extends JFrame {
    // Injeção do serviço de segurança
    private final UsuarioService usuarioService = AppConfig.usuarioService();

//    private JTextField txtNome;
//    private JTextField txtCpfCnpj;
//    private JTextField txtLogin;
//    private JPasswordField txtSenha;
//    private JComboBox<String> cmbTipo;

    JTextField textNome;
    JTextField textCpf;
    JTextField textLogin;
    JPasswordField textSenha;
    JComboBox<String> cmbTipo;


    public CadastroUsuario() {
        initComponets();
    }

    public void initComponets(){
        setTitle("Cadastro de Usuário");
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

    private void salvarUsuario() {
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

            usuarioService.cadastrarUsuario(novoUsuario);

            JOptionPane.showMessageDialog(this,
                    "Usuário " + tipo + " salvo com sucesso! Login: " + login,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

        JLabel title = new JLabel("Cadastro de Usuário");

        title.setBounds(450,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel1 = new JPanel(new GridLayout(5, 1, 0, 20));
        panel1.setBounds(80, 180, 150, 320);
        panel1.setBackground(new Color(10,20,30));

        JLabel label1 = new JLabel("Nome");
        label1.setForeground(new Color(240,246,252));
        label1.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel label2 = new JLabel("CPF/CNPJ");
        label2.setForeground(new Color(240,246,252));
        label2.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel label3 = new JLabel("Login");
        label3.setForeground(new Color(240,246,252));
        label3.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel label4 = new JLabel("Senha");
        label4.setForeground(new Color(240,246,252));
        label4.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel label5 = new JLabel("Tipo");
        label5.setForeground(new Color(240,246,252));
        label5.setFont(new Font("Roboto", Font.PLAIN, 20));

        panel1.add(label1);
        panel1.add(label2);
        panel1.add(label3);
        panel1.add(label4);
        panel1.add(label5);

        return panel1;
    }

    public JPanel midPanel2(){  

        JPanel panel2 = new JPanel(new GridLayout(5, 1, 0, 20));
        panel2.setBounds(270, 180, 860, 320);
        panel2.setBackground(new Color(10,20,30));


        textNome = new JTextField();
        textNome.setCaretColor(Color.green);
        textNome.setBackground(new Color(50,50,50));
        textNome.setForeground(new Color(240,246,252));
        textNome.setFont(new Font(null, Font.PLAIN, 20));
        //textNome.setText("Fulano da Silva");

        textCpf = new JTextField();
        textCpf.setCaretColor(Color.green);
        textCpf.setBackground(new Color(50,50,50));
        textCpf.setForeground(new Color(240,246,252));
        textCpf.setFont(new Font(null, Font.PLAIN, 20));
        //textCpf.setText("XXX.XXX.XXX-XX");

        textLogin = new JTextField();
        textLogin.setCaretColor(Color.green);
        textLogin.setBackground(new Color(50,50,50));
        textLogin.setForeground(new Color(240,246,252));
        textLogin.setFont(new Font(null, Font.PLAIN, 20));
        //textLogin.setText("Ativo");

        textSenha = new JPasswordField();
        textSenha.setCaretColor(Color.green);
        textSenha.setBackground(new Color(50,50,50));
        textSenha.setForeground(new Color(240,246,252));
        textSenha.setFont(new Font(null, Font.PLAIN, 20));
        //textSenha.setText("Recrutamento");

        cmbTipo = new JComboBox<>(new String[]{"Administrador", "Gestor", "Recrutador", "Funcionario", "Candidato"});
        cmbTipo.setBackground(new Color(50,50,50));
        cmbTipo.setForeground(new Color(240,246,252));
        cmbTipo.setFont(new Font(null, Font.PLAIN, 20));

        panel2.add(textNome);
        panel2.add(textCpf);
        panel2.add(textLogin);
        panel2.add(textSenha);
        panel2.add(cmbTipo);

        return panel2;
    }

    JButton botao;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(10,20,30));
        panel.setBounds(300,540,1280,120);

        botao = new JButton("Cadastrar");

        botao.setBounds(250,30,200,45);
        botao.setBackground(new Color(21,27,35));
        botao.setForeground(new Color(240,246,252));
        botao.setFocusable(false);
        //botao.addActionListener(this);
        botao.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botao);

        botao.addActionListener(e -> salvarUsuario());

        return panel;
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//
////        if (e.getSource()==botao) {
////
////            this.dispose();
////        }
//
//        botao.addActionListener(e -> salvarUsuario());
//        //btnCancelar.addActionListener(e -> dispose());
//
//
//    }

}
