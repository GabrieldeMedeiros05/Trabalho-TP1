package main.ui;

import Candidatura.ui.MenuCandidatura;
import Financeiro.ui.TelaFinanceiro;
import AdministracaoGestao.ui.MenuGestao;
import Seguranca.persistencia.UsuarioRepository;
import Seguranca.persistencia.csv.UsuarioRepositoryCsv;
import Seguranca.servico.UsuarioService;
import recrutamento.servico.RecrutamentoService;
import recrutamento.ui.RecrutamentoModuleConfig;
import recrutamento.ui.TelaMenuRecrutamento;
import Seguranca.dominio.Usuario;
import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

/**
 * Menu principal da aplicação.
 */
public class MenuPrincipal extends JFrame {

    private final Usuario usuarioLogado;

    private JButton btnAdminGestao;
    private JButton btnFinanceiro;
    private JButton btnRecrutamento;
    private JButton btnCandidatura;
    private JButton btnCadastrarUsuario; // NOVO CAMPO
    private JButton sairButton;

    public MenuPrincipal(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        setTitle("Sistema de Gestão Empresarial (HR)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lbl = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);

        // Layout para 5 botões
        JPanel grid = new JPanel(new GridLayout(5, 1, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        btnAdminGestao = new JButton("1. Administração e Gestão (Em Construção)");
        btnFinanceiro = new JButton("2. Módulo Financeiro");
        btnCandidatura = new JButton("3. Candidatura");
        btnRecrutamento = new JButton("4. Recrutamento");
        btnCadastrarUsuario = new JButton("4. Cadastrar Novo Usuário (RH/Admin)"); // NOVO

        //btnAdminGestao.setEnabled(false);

        grid.add(btnAdminGestao);
        grid.add(btnFinanceiro);
        grid.add(btnRecrutamento);
        grid.add(btnCandidatura);
        grid.add(btnCadastrarUsuario); // Adicionado

        add(grid, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        sairButton = new JButton("Sair do Sistema");
        footer.add(sairButton);
        add(footer, BorderLayout.SOUTH);

        configurarListeners();

        pack();
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    private void configurarListeners() {
        btnAdminGestao.addActionListener(e -> abrirModuloGestao());
        btnFinanceiro.addActionListener(e -> abrirModuloFinanceiro());
        btnCandidatura.addActionListener(e -> abrirModuloCandidatura());
        btnRecrutamento.addActionListener(e -> abrirModuloRecrutamento());
        btnCadastrarUsuario.addActionListener(e -> abrirCadastroUsuario()); // NOVO LISTENER
        sairButton.addActionListener(e -> dispose());
    }

    private void abrirModuloRecrutamento() {

        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuário não informado. Faça login novamente.",
                    "Sessão inválida",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Serviço do módulo Recrutamento
        RecrutamentoService service = RecrutamentoModuleConfig.service();

        // Serviço do módulo Segurança (para listar recrutadores)
        UsuarioRepository usuarioRepository =
                new UsuarioRepositoryCsv(Path.of("data/usuarios.csv"));
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);

        // Configura o usuário logado dentro do RecrutamentoService
        service.configurarUsuarioLogado(usuarioLogado);

        // Abre o menu do módulo Recrutamento, passando os dois serviços
        TelaMenuRecrutamento menu = new TelaMenuRecrutamento(service, usuarioService);
        menu.setLocationRelativeTo(this);

        // esconde o menu principal
        this.setVisible(false);

        // quando a tela de recrutamento for fechada, reexibe o menu principal
        menu.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                MenuPrincipal.this.setVisible(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                MenuPrincipal.this.setVisible(true);
            }
        });

        menu.setVisible(true);
    }

    private void abrirModuloCandidatura() {
        MenuCandidatura menuCandidatura = new MenuCandidatura();
        menuCandidatura.setVisible(true);
    }

    private void abrirCadastroUsuario() {
        TelaCadastroUsuario tela = new TelaCadastroUsuario();
        tela.setVisible(true);
    }

    private void abrirModuloFinanceiro() {
        TelaFinanceiro tela = new TelaFinanceiro();
        tela.setVisible(true);
    }

    private void abrirModuloGestao() {
        MenuGestao tela = new MenuGestao();
        tela.setVisible(true);
    }
}