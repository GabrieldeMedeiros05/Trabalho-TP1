package recrutamento.ui;

import recrutamento.dominio.RegimeContratacao;
import recrutamento.dominio.StatusVaga;
import recrutamento.dominio.Vaga;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tela de cadastro e edição de vagas.
 */
public class TelaCadastroVaga extends JFrame {

    private final RecrutamentoService service;
    private Vaga vagaEdicao; // null = cadastro, != null = edição

    // Mapa cpf -> label ("Nome (CPF)") para os recrutadores disponíveis
    private final Map<String, String> mapaRecrutadores;

    // Lista de vagas
    private DefaultListModel<Vaga> listaModel;
    private JList<Vaga> listaVagas;
    private JButton btnEditarSelecionada;
    private JButton btnNovaVaga;
    private JButton btnExcluirSelecionada;

    // Campos do formulário
    private JTextField txtCargo;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextArea txtRequisitos;
    private JComboBox<RegimeContratacao> comboRegime;
    private JComboBox<StatusVaga> comboStatus;
    private JComboBox<String> comboRecrutador;

    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnCancelar;

    /**
     * Construtor padrão: não recebe lista de recrutadores.
     * A combo de recrutador terá apenas "Nenhum".
     */
    public TelaCadastroVaga(RecrutamentoService service) {
        this(service, new LinkedHashMap<>());
    }
    public TelaCadastroVaga(RecrutamentoService service, Vaga vagaEdicaoInicial) {
        // Reaproveita toda a inicialização padrão
        this(service);
        // Marca que estamos editando uma vaga existente
        this.vagaEdicao = vagaEdicaoInicial;
        // Ajusta o texto do botão e preenche os campos
        btnSalvar.setText("Atualizar");
        preencherCampos();
    }

    /**
     * Construtor que recebe o mapa de recrutadores (cpf -> "Nome (CPF)").
     */
    public TelaCadastroVaga(RecrutamentoService service, Map<String, String> mapaRecrutadores) {
        this.service = service;
        this.vagaEdicao = null;
        this.mapaRecrutadores = (mapaRecrutadores != null) ? mapaRecrutadores : new LinkedHashMap<>();

        setTitle("Cadastro / Edição de Vagas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 480);
        setResizable(false);
        setLocationRelativeTo(null);

        montarComponentes();
        carregarRecrutadores();
        carregarVagas();
    }

    private void montarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Painel esquerdo: lista de vagas
        JPanel painelLista = new JPanel(new BorderLayout(5, 5));
        painelLista.setBorder(BorderFactory.createTitledBorder("Vagas cadastradas"));

        listaModel = new DefaultListModel<>();
        listaVagas = new JList<>(listaModel);
        listaVagas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer: mostra "cargo - dataAbertura - codigo"
        listaVagas.setCellRenderer(new DefaultListCellRenderer() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vaga) {
                    Vaga v = (Vaga) value;
                    String data = "";
                    if (v.getDataAbertura() != null) {
                        data = v.getDataAbertura().format(fmt);
                    }
                    label.setText(v.getCargo() + " - " + data + " - " + v.getCodigo());
                }
                return label;
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaVagas);
        painelLista.add(scrollLista, BorderLayout.CENTER);

        JPanel painelBotoesLista = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNovaVaga = new JButton("Nova vaga");
        btnEditarSelecionada = new JButton("Editar selecionada");
        btnExcluirSelecionada = new JButton("Excluir selecionada");

        painelBotoesLista.add(btnNovaVaga);
        painelBotoesLista.add(btnEditarSelecionada);
        painelBotoesLista.add(btnExcluirSelecionada);

        painelLista.add(painelBotoesLista, BorderLayout.SOUTH);

        // Painel direito: formulário
        JPanel painelForm = new JPanel(new BorderLayout(5, 5));
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados da vaga"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int linha = 0;

        // Cargo
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Cargo:"), gbc);

        gbc.gridx = 1;
        txtCargo = new JTextField();
        form.add(txtCargo, gbc);

        // Departamento
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Departamento:"), gbc);

        gbc.gridx = 1;
        txtDepartamento = new JTextField();
        form.add(txtDepartamento, gbc);

        // Salário base
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Salário Base (R$):"), gbc);

        gbc.gridx = 1;
        txtSalario = new JTextField();
        form.add(txtSalario, gbc);

        // Regime de contratação
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Regime de Contratação:"), gbc);

        gbc.gridx = 1;
        comboRegime = new JComboBox<>(RegimeContratacao.values());
        form.add(comboRegime, gbc);

        // Status da vaga
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Status da Vaga:"), gbc);

        gbc.gridx = 1;
        comboStatus = new JComboBox<>(StatusVaga.values());
        form.add(comboStatus, gbc);

        // Recrutador responsável
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Recrutador responsável:"), gbc);

        gbc.gridx = 1;
        comboRecrutador = new JComboBox<>();
        form.add(comboRecrutador, gbc);

        // Requisitos
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.anchor = GridBagConstraints.NORTH;
        form.add(new JLabel("Requisitos:"), gbc);

        gbc.gridx = 1;
        txtRequisitos = new JTextArea(5, 20);
        txtRequisitos.setLineWrap(true);
        txtRequisitos.setWrapStyleWord(true);
        JScrollPane scrollReq = new JScrollPane(txtRequisitos);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        form.add(scrollReq, gbc);

        painelForm.add(form, BorderLayout.CENTER);

        // Botões formulário
        JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLimpar = new JButton("Limpar");
        btnCancelar = new JButton("Fechar");
        btnSalvar = new JButton("Salvar");

        painelBotoesForm.add(btnLimpar);
        painelBotoesForm.add(btnCancelar);
        painelBotoesForm.add(btnSalvar);

        painelForm.add(painelBotoesForm, BorderLayout.SOUTH);

        painelPrincipal.add(painelLista, BorderLayout.WEST);
        painelPrincipal.add(painelForm, BorderLayout.CENTER);

        add(painelPrincipal);

        configurarAcoes();
    }

    private void configurarAcoes() {
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvar());

        btnNovaVaga.addActionListener(e -> {
            vagaEdicao = null;
            btnSalvar.setText("Salvar");
            limparCampos();
            listaVagas.clearSelection();
        });

        btnEditarSelecionada.addActionListener(e -> carregarParaEdicao());
        btnExcluirSelecionada.addActionListener(e -> excluirSelecionada());
    }

    private void carregarVagas() {
        listaModel.clear();
        List<Vaga> vagas = service.listarVagas();
        for (Vaga v : vagas) {
            listaModel.addElement(v);
        }
    }

    private void carregarRecrutadores() {
        comboRecrutador.removeAllItems();
        comboRecrutador.addItem("Nenhum");
        for (String label : mapaRecrutadores.values()) {
            comboRecrutador.addItem(label);
        }
        // Status padrão ao abrir: ABERTA
        if (comboStatus.getItemCount() > 0) {
            comboStatus.setSelectedItem(StatusVaga.ABERTA);
        }
    }

    private void carregarParaEdicao() {
        Vaga selecionada = listaVagas.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma vaga na lista para editar.",
                    "Nenhuma vaga selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.vagaEdicao = selecionada;
        btnSalvar.setText("Atualizar");
        preencherCampos();
    }

    private void excluirSelecionada() {
        Vaga selecionada = listaVagas.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma vaga na lista para excluir.",
                    "Nenhuma vaga selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opc = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir a vaga?\n" +
                        selecionada.getCargo(),
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.excluirVaga(selecionada.getId());
            vagaEdicao = null;
            btnSalvar.setText("Salvar");
            limparCampos();
            carregarVagas();
            JOptionPane.showMessageDialog(this,
                    "Vaga excluída com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

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
                    "Erro inesperado ao excluir a vaga.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void preencherCampos() {
        if (vagaEdicao == null) return;

        txtCargo.setText(vagaEdicao.getCargo());
        txtDepartamento.setText(vagaEdicao.getDepartamento());
        txtSalario.setText(String.valueOf(vagaEdicao.getSalarioBase()));
        txtRequisitos.setText(vagaEdicao.getRequisitos());
        comboRegime.setSelectedItem(vagaEdicao.getRegime());
        comboStatus.setSelectedItem(vagaEdicao.getStatus());

        String cpfRecrutador = vagaEdicao.getRecrutadorResponsavelCpf();
        if (cpfRecrutador == null || cpfRecrutador.isBlank()) {
            comboRecrutador.setSelectedItem("Nenhum");
        } else {
            String label = mapaRecrutadores.get(cpfRecrutador);
            if (label != null) {
                comboRecrutador.setSelectedItem(label);
            } else {
                comboRecrutador.setSelectedItem("Nenhum");
            }
        }
    }

    private void limparCampos() {
        txtCargo.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtRequisitos.setText("");
        if (comboRegime.getItemCount() > 0) {
            comboRegime.setSelectedIndex(0);
        }
        if (comboStatus.getItemCount() > 0) {
            comboStatus.setSelectedItem(StatusVaga.ABERTA);
        }
        comboRecrutador.setSelectedItem("Nenhum");
    }

    private String obterCpfRecrutadorSelecionado() {
        String selecionado = (String) comboRecrutador.getSelectedItem();
        if (selecionado == null || "Nenhum".equals(selecionado)) {
            return null;
        }
        return mapaRecrutadores.entrySet().stream()
                .filter(e -> e.getValue().equals(selecionado))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void salvar() {
        try {
            String cargo = txtCargo.getText().trim();
            String departamento = txtDepartamento.getText().trim();
            String salarioStr = txtSalario.getText().trim();
            String requisitos = txtRequisitos.getText().trim();
            RegimeContratacao regime = (RegimeContratacao) comboRegime.getSelectedItem();
            StatusVaga statusSelecionado = (StatusVaga) comboStatus.getSelectedItem();
            String cpfRecrutador = obterCpfRecrutadorSelecionado();

            if (cargo.isEmpty() || departamento.isEmpty() || salarioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha pelo menos Cargo, Departamento e Salário.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double salarioBase;
            try {
                salarioBase = Double.parseDouble(salarioStr.replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Salário inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (vagaEdicao == null) {
                // Cadastro
                Vaga nova = service.criarVaga(cargo, departamento, salarioBase, requisitos, regime);

                // Status (por regra, criamos ABERTA; se quiser outro, chamamos editar)
                if (statusSelecionado != null && statusSelecionado != StatusVaga.ABERTA) {
                    service.editarVaga(
                            nova.getId(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            statusSelecionado
                    );
                }

                // Atribui recrutador
                if (cpfRecrutador != null && !cpfRecrutador.isBlank()) {
                    service.atribuirRecrutador(nova.getId(), cpfRecrutador);
                }

                JOptionPane.showMessageDialog(this,
                        "Vaga cadastrada com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                // Edição
                service.editarVaga(
                        vagaEdicao.getId(),
                        cargo,
                        departamento,
                        salarioBase,
                        requisitos,
                        regime,
                        statusSelecionado
                );

                // Atualiza recrutador (pode ser null para remover)
                service.atribuirRecrutador(vagaEdicao.getId(), cpfRecrutador);

                JOptionPane.showMessageDialog(this,
                        "Vaga atualizada com sucesso.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            vagaEdicao = null;
            btnSalvar.setText("Salvar");
            limparCampos();
            carregarVagas();

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
                    "Erro inesperado ao salvar a vaga.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
