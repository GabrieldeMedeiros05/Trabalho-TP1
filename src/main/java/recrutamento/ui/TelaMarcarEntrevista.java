package recrutamento.ui;

import recrutamento.dominio.Entrevista;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela para marcação e gerenciamento de entrevistas.
 * - Seleciona uma candidatura (CPF - Cargo da vaga)
 * - Lista entrevistas da candidatura
 * - Permite criar, editar, excluir, atribuir nota e registrar parecer
 */
public class TelaMarcarEntrevista extends JFrame {

    private final RecrutamentoService service;

    // Combo de candidaturas
    private JComboBox<CandidaturaDTO> comboCandidaturas;

    // Tabela de entrevistas
    private JTable tabelaEntrevistas;
    private DefaultTableModel entrevistasModel;
    private Entrevista entrevistaSelecionada;

    // Campos do formulário
    private JTextField txtDataHora;
    private JTextField txtAvaliador;
    private JTextField txtObservacoes;
    private JSpinner spNota;
    private JComboBox<String> comboParecer;

    // Botões
    private JButton btnAgendar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;
    private JButton btnFechar;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Valores padronizados de parecer
    private static final String PARECER_EM_ANALISE = "";
    private static final String PARECER_SEGUNDA = "Segunda entrevista";
    private static final String PARECER_ELIMINADO = "Eliminado";
    private static final String PARECER_SOLIC_CONTRATACAO = "Solicitar contratação";

    public TelaMarcarEntrevista(RecrutamentoService service) {
        this.service = service;

        setTitle("Entrevistas - Marcar / Gerenciar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        montarComponentes();
        configurarAcoes();
        carregarCandidaturas();
    }

    private void montarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(8, 8));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========================
        // Linha 1: seleção de candidatura
        // ========================
        JPanel painelCandidatura = new JPanel(new BorderLayout(5, 5));

        JLabel lblCand = new JLabel("Candidato / Vaga:");
        comboCandidaturas = new JComboBox<>();
        comboCandidaturas.setPrototypeDisplayValue(
                new CandidaturaDTO("XXXXXX", "VAGAID", "00000000000", "EM_ANALISE")
        );
        comboCandidaturas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CandidaturaDTO) {
                    CandidaturaDTO c = (CandidaturaDTO) value;
                    String rotulo = service.montarRotuloCandidatura(c);
                    if (rotulo == null || rotulo.isBlank()) {
                        rotulo = c.getCandidatoCpf() + " - " + c.getVagaId();
                    }
                    label.setText(rotulo);
                }
                return label;
            }
        });

        painelCandidatura.add(lblCand, BorderLayout.WEST);
        painelCandidatura.add(comboCandidaturas, BorderLayout.CENTER);

        painelPrincipal.add(painelCandidatura, BorderLayout.NORTH);

        // ========================
        // Centro: tabela + formulário
        // ========================
        JPanel centro = new JPanel(new BorderLayout(8, 8));

        // Tabela de entrevistas (agora com Parecer)
        entrevistasModel = new DefaultTableModel(
                new Object[]{"ID", "Data/Hora", "Avaliador", "Nota", "Parecer"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // edição só pelo formulário
            }
        };

        tabelaEntrevistas = new JTable(entrevistasModel);
        JScrollPane scrollTabela = new JScrollPane(tabelaEntrevistas);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Entrevistas da candidatura"));

        centro.add(scrollTabela, BorderLayout.CENTER);

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados da entrevista"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int linha = 0;

        // Data/Hora
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelForm.add(new JLabel("Data/Hora (dd/MM/aaaa HH:mm):"), gbc);

        gbc.gridx = 1;
        txtDataHora = new JTextField();
        painelForm.add(txtDataHora, gbc);

        // Avaliador
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelForm.add(new JLabel("Avaliador:"), gbc);

        gbc.gridx = 1;
        txtAvaliador = new JTextField();
        painelForm.add(txtAvaliador, gbc);

        // Nota
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelForm.add(new JLabel("Nota (0 a 10):"), gbc);

        gbc.gridx = 1;
        spNota = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10.0, 0.5));
        painelForm.add(spNota, gbc);

        // Parecer
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelForm.add(new JLabel("Parecer:"), gbc);

        gbc.gridx = 1;
        comboParecer = new JComboBox<>();
        comboParecer.addItem(PARECER_EM_ANALISE);          // vazio = em análise
        comboParecer.addItem(PARECER_SEGUNDA);
        comboParecer.addItem(PARECER_ELIMINADO);
        comboParecer.addItem(PARECER_SOLIC_CONTRATACAO);
        painelForm.add(comboParecer, gbc);

        // Observações (não persistidas, apenas log)
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        painelForm.add(new JLabel("Observações:"), gbc);

        gbc.gridx = 1;
        txtObservacoes = new JTextField();
        painelForm.add(txtObservacoes, gbc);

        centro.add(painelForm, BorderLayout.SOUTH);

        painelPrincipal.add(centro, BorderLayout.CENTER);

        // ========================
        // Botões
        // ========================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLimpar = new JButton("Limpar");
        btnExcluir = new JButton("Excluir selecionada");
        btnAtualizar = new JButton("Atualizar selecionada");
        btnAgendar = new JButton("Agendar nova");
        btnFechar = new JButton("Fechar");

        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnAgendar);
        painelBotoes.add(btnFechar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void configurarAcoes() {
        btnFechar.addActionListener(e -> dispose());
        btnLimpar.addActionListener(e -> limparCampos());

        btnAgendar.addActionListener(e -> agendarEntrevista());
        btnAtualizar.addActionListener(e -> atualizarEntrevista());
        btnExcluir.addActionListener(e -> excluirEntrevista());

        comboCandidaturas.addActionListener(e -> carregarEntrevistasDaCandidatura());

        tabelaEntrevistas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarEntrevistaSelecionada();
            }
        });
    }

    private void carregarCandidaturas() {
        try {
            comboCandidaturas.removeAllItems();

            List<CandidaturaDTO> lista = service.listarCandidaturasParaEntrevista();
            for (CandidaturaDTO c : lista) {
                comboCandidaturas.addItem(c);
            }

            if (comboCandidaturas.getItemCount() > 0) {
                comboCandidaturas.setSelectedIndex(0);
                carregarEntrevistasDaCandidatura();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar candidaturas.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarEntrevistasDaCandidatura() {
        entrevistasModel.setRowCount(0);
        entrevistaSelecionada = null;
        limparCampos(false);

        CandidaturaDTO selecao = (CandidaturaDTO) comboCandidaturas.getSelectedItem();
        if (selecao == null) {
            return;
        }

        try {
            List<Entrevista> entrevistas = service.listarEntrevistasDaCandidatura(selecao.getId());
            for (Entrevista e : entrevistas) {
                String dataStr = e.getDataHora() != null
                        ? e.getDataHora().format(formatter)
                        : "";
                Double nota = e.getNota();
                String parecer = e.getParecer();

                entrevistasModel.addRow(new Object[]{
                        e.getId(),
                        dataStr,
                        e.getAvaliador(),
                        nota,
                        parecer
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar entrevistas.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void carregarEntrevistaSelecionada() {
        int linhaSel = tabelaEntrevistas.getSelectedRow();
        if (linhaSel < 0) {
            entrevistaSelecionada = null;
            habilitarEdicao(true);
            return;
        }

        String entrevistaId = (String) entrevistasModel.getValueAt(linhaSel, 0);

        try {
            entrevistaSelecionada = service.buscarEntrevistaPorId(entrevistaId);
            if (entrevistaSelecionada == null) {
                habilitarEdicao(true);
                return;
            }

            // Preenche o formulário
            if (entrevistaSelecionada.getDataHora() != null) {
                txtDataHora.setText(entrevistaSelecionada.getDataHora().format(formatter));
            } else {
                txtDataHora.setText("");
            }

            txtAvaliador.setText(entrevistaSelecionada.getAvaliador() != null
                    ? entrevistaSelecionada.getAvaliador()
                    : "");

            Double nota = entrevistaSelecionada.getNota();
            spNota.setValue(nota != null ? nota : 0.0);

            String parecer = entrevistaSelecionada.getParecer();
            if (parecer == null || parecer.isBlank()) {
                comboParecer.setSelectedItem(PARECER_EM_ANALISE);
            } else {
                // tenta selecionar exatamente o texto salvo; se não existir, deixa em análise
                boolean found = false;
                for (int i = 0; i < comboParecer.getItemCount(); i++) {
                    if (parecer.equalsIgnoreCase(comboParecer.getItemAt(i))) {
                        comboParecer.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    comboParecer.setSelectedItem(PARECER_EM_ANALISE);
                }
            }

            // Observações continuam apenas em memória (não persistidas)

            // Bloqueio de edição conforme parecer
            boolean bloqueada = isEntrevistaBloqueada(entrevistaSelecionada);
            habilitarEdicao(!bloqueada);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar a entrevista selecionada.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Entrevista está "travada" se parecer for Eliminado ou Solicitar contratação.
     */
    private boolean isEntrevistaBloqueada(Entrevista e) {
        if (e == null) return false;
        String p = e.getParecer();
        if (p == null) return false;

        String pNorm = p.trim().toLowerCase();
        return pNorm.equals("eliminado")
                || pNorm.equals("solicitar contratação")
                || pNorm.equals("solicitar contratacao");
    }

    /**
     * Habilita ou não os campos de edição e o botão Atualizar.
     * (Excluir continua liberado, como combinado.)
     */
    private void habilitarEdicao(boolean habilitar) {
        txtDataHora.setEnabled(habilitar);
        txtAvaliador.setEnabled(habilitar);
        spNota.setEnabled(habilitar);
        comboParecer.setEnabled(habilitar);
        txtObservacoes.setEnabled(habilitar);
        btnAtualizar.setEnabled(habilitar);
    }

    private void limparCampos() {
        limparCampos(true);
    }

    private void limparCampos(boolean limparSelecaoTabela) {
        txtDataHora.setText("");
        txtAvaliador.setText("");
        txtObservacoes.setText("");
        spNota.setValue(0.0);
        comboParecer.setSelectedItem(PARECER_EM_ANALISE);

        entrevistaSelecionada = null;
        habilitarEdicao(true);

        if (limparSelecaoTabela) {
            tabelaEntrevistas.clearSelection();
        }
    }

    private LocalDateTime lerDataHora() {
        String dataHoraStr = txtDataHora.getText().trim();
        if (dataHoraStr.isEmpty()) {
            throw new RegraNegocioException("Informe a data e hora da entrevista.");
        }

        try {
            return LocalDateTime.parse(dataHoraStr, formatter);
        } catch (DateTimeParseException ex) {
            throw new RegraNegocioException("Data/Hora inválida. Use o formato dd/MM/aaaa HH:mm.");
        }
    }

    private void agendarEntrevista() {
        try {
            CandidaturaDTO selecao = (CandidaturaDTO) comboCandidaturas.getSelectedItem();
            if (selecao == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma candidatura.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDateTime dataHora = lerDataHora();
            String avaliador = txtAvaliador.getText().trim();
            if (avaliador.isEmpty()) {
                throw new RegraNegocioException("Informe o avaliador.");
            }

            double nota = (Double) spNota.getValue();
            String observacoes = txtObservacoes.getText().trim();
            String parecer = (String) comboParecer.getSelectedItem();
            if (parecer != null && parecer.trim().isEmpty()) {
                parecer = null; // em análise
            }

            // Cria nova entrevista (agendar) com nota e parecer
            service.agendarEntrevista(selecao.getId(), dataHora, avaliador, nota);

            if (!observacoes.isEmpty()) {
                System.out.println("[OBS ENTREVISTA NOVA] " + observacoes);
            }

            JOptionPane.showMessageDialog(this,
                    "Entrevista agendada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            carregarEntrevistasDaCandidatura();
            limparCampos();

        } catch (AutorizacaoException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de autorização",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de negócio",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado ao agendar a entrevista.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarEntrevista() {
        if (entrevistaSelecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma entrevista na tabela para atualizar.",
                    "Nenhuma entrevista selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se por algum motivo chegar aqui com entrevista bloqueada, aborta
        if (isEntrevistaBloqueada(entrevistaSelecionada)) {
            JOptionPane.showMessageDialog(this,
                    "Esta entrevista está com parecer '" + entrevistaSelecionada.getParecer() +
                            "' e não pode ser editada.",
                    "Edição bloqueada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDateTime dataHora = lerDataHora();
            String avaliador = txtAvaliador.getText().trim();
            if (avaliador.isEmpty()) {
                throw new RegraNegocioException("Informe o avaliador.");
            }

            double nota = (Double) spNota.getValue();
            String observacoes = txtObservacoes.getText().trim();
            String parecer = (String) comboParecer.getSelectedItem();
            if (parecer != null && parecer.trim().isEmpty()) {
                parecer = null;
            }

            service.atualizarEntrevista(
                    entrevistaSelecionada.getId(),
                    dataHora,
                    avaliador,
                    nota,
                    parecer
            );

            if (!observacoes.isEmpty()) {
                System.out.println("[OBS ENTREVISTA ATUALIZADA] " + observacoes);
            }

            JOptionPane.showMessageDialog(this,
                    "Entrevista atualizada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            carregarEntrevistasDaCandidatura();
            limparCampos();

        } catch (AutorizacaoException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de autorização",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de negócio",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado ao atualizar a entrevista.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void excluirEntrevista() {
        if (entrevistaSelecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma entrevista na tabela para excluir.",
                    "Nenhuma entrevista selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opc = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta entrevista?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.excluirEntrevista(entrevistaSelecionada.getId());

            JOptionPane.showMessageDialog(this,
                    "Entrevista excluída com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            carregarEntrevistasDaCandidatura();
            limparCampos();

        } catch (AutorizacaoException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de autorização",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (RegraNegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de negócio",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado ao excluir a entrevista.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
