package recrutamento.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuRecrutamento extends JFrame {

    // Declaração manual dos componentes
    private JPanel contentPane;
    private JLabel lblTitulo;
    private JButton sairButton;
    private JButton btnVagas;
    private JButton btnVagasCadastro;
    private JButton btnCandidatosListar;
    private JButton btnCandidatosCadastrar;
    private JButton btnCandidaturaRealizar;
    private JButton btnEntrevistaMarcar;
    private JButton btnContratacaoSolicitar;
    private JButton btnContratacaoConsultar;

    public MenuRecrutamento() {
        // --- 1. Inicialização dos Componentes (AGORA INICIALIZADOS!) ---
        criarComponentes();

        // --- 2. Configuração do Frame ---
        setTitle("Recrutamento • Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configura o layout principal do JFrame
        setLayout(new BorderLayout());

        // --- 3. Montagem da UI (usando layout manual) ---
        montarLayout();

        // --- 4. Configuração de Ações ---
        configurarListeners();

        // Finalização do Frame
        pack(); // Ajusta o tamanho do frame aos componentes
        setSize(500, 400); // Define um tamanho fixo
        setLocationRelativeTo(null);
    }

    // Método que CRIA e INICIALIZA todos os JButtons
    private void criarComponentes() {
        // Painel principal para agrupar botões
        contentPane = new JPanel(new GridLayout(6, 2, 10, 10)); // 6 linhas, 2 colunas, com espaçamento

        // Rótulo
        lblTitulo = new JLabel("Menu de Recrutamento", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Botões (texto ajustado para o layout)
        btnVagas = new JButton("Vagas – Listar/Filtrar");
        btnVagasCadastro = new JButton("Vagas – Cadastrar/Editar");
        btnCandidatosListar = new JButton("Candidatos – Listar");
        btnCandidatosCadastrar = new JButton("Candidatos – Cadastrar");
        btnCandidaturaRealizar = new JButton("Realizar Candidatura");
        btnEntrevistaMarcar = new JButton("Marcar Entrevista");
        btnContratacaoSolicitar = new JButton("Solicitar Contratação");
        btnContratacaoConsultar = new JButton("Consultar Contratações");

        // Botão Sair
        sairButton = new JButton("Sair do Sistema");
    }

    // Método para organizar os componentes no Frame
    private void montarLayout() {
        // Adiciona o título ao topo (NORTH)
        add(lblTitulo, BorderLayout.NORTH);

        // Adiciona os botões ao painel de conteúdo (CENTER)
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem
        contentPane.add(btnVagas);
        contentPane.add(btnVagasCadastro);
        contentPane.add(btnCandidatosListar);
        contentPane.add(btnCandidatosCadastrar);
        contentPane.add(btnCandidaturaRealizar);
        contentPane.add(btnEntrevistaMarcar);
        contentPane.add(btnContratacaoSolicitar);
        contentPane.add(btnContratacaoConsultar);
        // Preenche o restante do grid
        contentPane.add(new JPanel());
        contentPane.add(new JPanel());
        contentPane.add(new JPanel());
        contentPane.add(new JPanel());

        add(contentPane, BorderLayout.CENTER);

        // Adiciona o botão Sair no rodapé (SOUTH)
        JPanel footerPanel = new JPanel();
        footerPanel.add(sairButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Método para configurar os listeners
    private void configurarListeners() {
        btnVagas.addActionListener(e -> info("Abrir: Vagas – Listar/Filtrar"));
        btnVagasCadastro.addActionListener(e -> info("Abrir: Vagas – Cadastrar/Editar"));
        btnCandidatosListar.addActionListener(e -> info("Abrir: Candidatos – Listar"));
        btnCandidatosCadastrar.addActionListener(e -> info("Abrir: Candidatos – Cadastrar"));
        btnCandidaturaRealizar.addActionListener(e -> info("Abrir: Realizar Candidatura"));
        btnEntrevistaMarcar.addActionListener(e -> info("Abrir: Marcar Entrevista"));
        btnContratacaoSolicitar.addActionListener(e -> info("Abrir: Solicitar Contratação"));
        btnContratacaoConsultar.addActionListener(e -> info("Abrir: Consultar Contratações"));

        if (sairButton != null) sairButton.addActionListener(e -> dispose());
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Protótipo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrir(JFrame frame) {
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuRecrutamento().setVisible(true));
    }
}