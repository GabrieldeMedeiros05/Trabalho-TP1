package recrutamento.ui;

import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu principal do módulo de Recrutamento.
 * Integra com:
 *  - RecrutamentoService (regra de negócio)
 *  - UsuarioService (para buscar recrutadores na criação/edição de vagas)
 */
public class TelaMenuRecrutamento extends JFrame {

    private final RecrutamentoService recrutamentoService;
    private final UsuarioService usuarioService;

    private JButton btnVagasListar;
    private JButton btnVagasCadastro;
    private JButton btnEntrevistas;
    private JButton btnSolicitarContratacao;
    private JButton btnConsultarContratacoes;
    private JButton btnFechar;

    public TelaMenuRecrutamento(RecrutamentoService recrutamentoService,
                                UsuarioService usuarioService) {
        this.recrutamentoService = recrutamentoService;
        this.usuarioService = usuarioService;

        setTitle("Módulo de Recrutamento - Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setSize(480, 380);
        setLocationRelativeTo(null);

        montarComponentes();
        configurarAcoes();
    }

    private void montarComponentes() {
        JLabel titulo = new JLabel("Módulo de Recrutamento", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 8, 8));
        botoes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnVagasListar = new JButton("Vagas - Listar/Filtrar");
        btnVagasCadastro = new JButton("Vagas - Cadastrar/Editar");
        btnEntrevistas = new JButton("Entrevistas");
        btnSolicitarContratacao = new JButton("Contratação - Solicitar");
        btnConsultarContratacoes = new JButton("Contratação - Consultar");

        botoes.add(btnVagasListar);
        botoes.add(btnVagasCadastro);
        botoes.add(btnEntrevistas);
        botoes.add(btnSolicitarContratacao);
        botoes.add(btnConsultarContratacoes);

        add(botoes, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFechar = new JButton("Fechar");
        rodape.add(btnFechar);
        add(rodape, BorderLayout.SOUTH);
    }

    private void configurarAcoes() {
        // Aqui você chama as telas específicas do módulo
        btnVagasListar.addActionListener(e -> abrirTelaListarVagas());
        btnVagasCadastro.addActionListener(e -> abrirTelaCadastroVagas());
        btnEntrevistas.addActionListener(e -> abrirTelaMarcarEntrevista());
        btnSolicitarContratacao.addActionListener(e -> abrirTelaSolicitarContratacao());
        btnConsultarContratacoes.addActionListener(e -> abrirTelaConsultarContratacoes());
        btnFechar.addActionListener(e -> dispose());
    }

    private void abrirTelaListarVagas() {
        // Ajuste o construtor de TelaListarVagas se ele ainda não receber o service
        TelaListarVagas tela = new TelaListarVagas(recrutamentoService);
        tela.setVisible(true);
    }

    private void abrirTelaCadastroVagas() {
        Map<String, String> mapaRecrutadores = carregarMapaRecrutadores();
        TelaCadastroVaga tela = new TelaCadastroVaga(recrutamentoService, mapaRecrutadores);
        tela.setVisible(true);
    }

    private void abrirTelaMarcarEntrevista() {
        TelaMarcarEntrevista tela = new TelaMarcarEntrevista(recrutamentoService);
        tela.setVisible(true);
    }

    private void abrirTelaSolicitarContratacao() {
        TelaSolicitarContratacao tela = new TelaSolicitarContratacao(recrutamentoService);
        tela.setVisible(true);
    }

    private void abrirTelaConsultarContratacoes() {
        TelaConsultarContratacoes tela = new TelaConsultarContratacoes(recrutamentoService);
        tela.setVisible(true);
    }

    /**
     * Monta um mapa cpf -> "Nome (CPF)" apenas para usuários com perfil de Recrutador.
     */
    private Map<String, String> carregarMapaRecrutadores() {
        Map<String, String> mapa = new LinkedHashMap<>();

        // Agora usamos o método específico:
        List<Usuario> usuarios = usuarioService.listarRecrutadores();

        for (Usuario u : usuarios) {
            String cpf = u.getCpf_cnpj(); // ou getCpf(), dependendo da sua classe
            String label = u.getNome() + " (" + cpf + ")";
            mapa.put(cpf, label);
        }

        return mapa;
    }
}
