package recrutamento.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TelaMenuRecrutamento extends JFrame {
    public TelaMenuRecrutamento() {
        setTitle("Recrutamento - Menu");
        setSize(480, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Módulo de Recrutamento", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 8, 8));

        Map<String, Supplier<JFrame>> rotas = new LinkedHashMap<>();
        rotas.put("Vagas - Listar/Filtrar", TelaListarVagas::new);
        rotas.put("Vagas - Cadastrar/Editar", TelaCadastroVaga::new);
        rotas.put("Candidatos - Listar", TelaListarCandidatos::new);
        rotas.put("Candidatos - Cadastrar", TelaCadastroCandidato::new);
        rotas.put("Candidatura - Realizar", TelaRealizarCandidatura::new);
        rotas.put("Entrevistas - Marcar", TelaMarcarEntrevista::new);
        rotas.put("Contratação - Solicitar", TelaSolicitarContratacao::new);
        rotas.put("Contratação - Consultar", TelaConsultarContratacoes::new);

        rotas.forEach((nome, fabrica) -> {
            JButton btn = new JButton(nome);
            btn.addActionListener(e -> fabrica.get().setVisible(true));
            botoes.add(btn);
        });

        add(botoes, BorderLayout.CENTER);
    }
}
