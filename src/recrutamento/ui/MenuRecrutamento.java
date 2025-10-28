package recrutamento.ui;

import javax.swing.*;

public class MenuRecrutamento extends JFrame {
    // === Campos gerados/bindados pelo GUI Designer (.form) ===
    private JPanel contentPane;                // root do formulário
    private JLabel lblTitulo;
    private JButton sairButton;
    private JButton btnVagas;                  // "Vagas – Listar/Filtrar"
    private JButton btnVagasCadastro;          // "Vagas – Cadastrar/Editar"
    private JButton btnCandidatosListar;       // "Candidatos – Listar"
    private JButton btnCandidatosCadastrar;    // "Candidatos – Cadastrar"
    private JButton btnCandidaturaRealizar;    // "Realizar Candidatura"
    private JButton btnEntrevistaMarcar;       // "Marcar Entrevista"
    private JButton btnContratacaoSolicitar;   // "Solicitar Contratação"
    private JButton btnContratacaoConsultar;   // "Consultar Contratações"

    public MenuRecrutamento() {
        // propriedades do frame
        setTitle("Recrutamento • Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 640);
        setLocationRelativeTo(null);

        // *** peça-chave: usar o painel do .form como content pane ***
        setContentPane(contentPane);

        // listeners (por enquanto, mostram apenas um diálogo;
        // troque por: abrir(new TelaListarVagas()); quando criar as telas)
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

    // *** main correto ***
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuRecrutamento().setVisible(true));
    }
}
