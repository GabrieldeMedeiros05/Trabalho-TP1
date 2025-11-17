package AdministracaoGestao.ui;

import Candidatura.excecoes.RegraNegocioException;
import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import utils.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static utils.Constantes.COR_DE_FUNDO;

public class ValidarCpfPesquisar extends JFrame{

    private final UsuarioService usuarioService = AppConfig.usuarioService();

    JTextField textNome;
    JTextField textCpf;

    public ValidarCpfPesquisar() {
        initComponets();
    }

    public void initComponets(){
        setTitle("Pesquisar Usuário");
        setSize(1280,720);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);

        setLocationRelativeTo(null);

        add(mainPanel());
    }

    private void validarCpf() {
        String nome = textNome.getText().trim();
        String cpf = textCpf.getText().trim();


        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Optional<Usuario> usuario = usuarioService.validarCpf(cpf, nome);

            dispose();
            new PesquisaUsuario(usuario);

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de Negócio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

        JLabel title = new JLabel("Pesquisar Usuário");

        title.setBounds(500,30,800,45);
        title.setForeground(new Color(240,246,252));
        title.setFont(new Font("M+ 2c black", Font.PLAIN, 40));

        panel.add(title);

        return panel;
    }

    public JPanel midPanel(){

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 70));
        panel.setBounds(80, 215, 150, 200);
        panel.setBackground(COR_DE_FUNDO);

        JLabel labelNome = new JLabel("Nome");
        labelNome.setForeground(new Color(240,246,252));
        labelNome.setFont(new Font("Roboto", Font.PLAIN, 20));

        JLabel labelCpf = new JLabel("CPF/CNPJ");
        labelCpf.setForeground(new Color(240,246,252));
        labelCpf.setFont(new Font("Roboto", Font.PLAIN, 20));

        panel.add(labelNome);
        panel.add(labelCpf);

        return panel;
    }

    public JPanel midPanel2(){

        JPanel panel2 = new JPanel(new GridLayout(2, 1, 0, 70));
        panel2.setBounds(250, 215, 860, 200);
        panel2.setBackground(COR_DE_FUNDO);


        textNome = new JTextField();
        textNome.setCaretColor(Color.green);
        textNome.setBackground(new Color(50,50,50));
        textNome.setForeground(new Color(240,246,252));
        textNome.setFont(new Font(null, Font.PLAIN, 20));

        textCpf = new JTextField();
        textCpf.setCaretColor(Color.green);
        textCpf.setBackground(new Color(50,50,50));
        textCpf.setForeground(new Color(240,246,252));
        textCpf.setFont(new Font(null, Font.PLAIN, 20));

        panel2.add(textNome);
        panel2.add(textCpf);

        return panel2;
    }

    JButton botaoExcluir;

    public JPanel lowerPanel(){

        JPanel panel = new JPanel(null);
        panel.setBackground(COR_DE_FUNDO);
        panel.setBounds(300,520,1280,120);


        botaoExcluir = new JButton("Pesquisar");
        botaoExcluir.setBounds(250,30,200,45);
        botaoExcluir.setBackground(new Color(21,27,35));
        botaoExcluir.setForeground(new Color(240,246,252));
        botaoExcluir.setFocusable(false);
        botaoExcluir.setFont(new Font("Roboto", Font.PLAIN, 17));

        panel.add(botaoExcluir);

        botaoExcluir.addActionListener(e -> validarCpf());

        return panel;
    }
}
