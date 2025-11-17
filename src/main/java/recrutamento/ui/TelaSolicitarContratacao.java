package recrutamento.ui;

import recrutamento.dominio.RegimeContratacao;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.excecoes.AutorizacaoException;
import recrutamento.excecoes.RegraNegocioException;
import recrutamento.servico.RecrutamentoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Tela para o RECRUTADOR solicitar a contratação de um candidato.
 * São exibidas apenas candidaturas que:
 *  - estão APROVADAS
 *  - possuem entrevista com parecer "Solicitar contratação"
 */
public class TelaSolicitarContratacao extends JFrame {

    private final RecrutamentoService service;

    // Componentes
    private JComboBox<CandidaturaDTO> comboCandidaturas;
    private JComboBox<RegimeContratacao> comboRegime;
    private JButton btnSolicitar;
    private JButton btnFechar;

    public TelaSolicitarContratacao(RecrutamentoService service) {
        this.service = service;

        setTitle("Contratação - Solicitar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        montarComponentes();
        configurarAcoes();
        carregarCandidaturasElegiveis();
    }

    private void montarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int linha = 0;

        // Linha 1 – Candidatura
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Candidatos com parecer 'Solicitar contratação':"), gbc);

        gbc.gridx = 1;
        comboCandidaturas = new JComboBox<>();
        comboCandidaturas.setPrototypeDisplayValue(
                new CandidaturaDTO("XXXXXX", "VAGAID", "00000000000", "APROVADO")
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
                    label.setText(rotulo + " [status: " + c.getStatus() + "]");
                }
                return label;
            }
        });
        form.add(comboCandidaturas, gbc);

        // Linha 2 – Regime de contratação
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel("Regime de contratação:"), gbc);

        gbc.gridx = 1;
        comboRegime = new JComboBox<>(RegimeContratacao.values());
        form.add(comboRegime, gbc);

        // Linha 3 – Observação / instrução
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblInfo = new JLabel(
                "<html><i>A data da solicitação será registrada automaticamente " +
                        "no registro de contratação.</i></html>"
        );
        form.add(lblInfo, gbc);

        painelPrincipal.add(form, BorderLayout.CENTER);

        // Rodapé – botões
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFechar = new JButton("Fechar");
        btnSolicitar = new JButton("Solicitar contratação");

        rodape.add(btnFechar);
        rodape.add(btnSolicitar);

        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void configurarAcoes() {
        btnFechar.addActionListener(e -> dispose());
        btnSolicitar.addActionListener(e -> solicitarContratacao());
    }

    /**
     * Carrega candidaturas que possuem entrevista com parecer "Solicitar contratação"
     * e estão aprovadas, usando o serviço de Recrutamento.
     */
    private void carregarCandidaturasElegiveis() {
        try {
            comboCandidaturas.removeAllItems();

            List<CandidaturaDTO> elegiveis = service.listarCandidaturasParaSolicitarContratacao();
            for (CandidaturaDTO c : elegiveis) {
                comboCandidaturas.addItem(c);
            }

            if (comboCandidaturas.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Não há candidaturas com parecer 'Solicitar contratação'.",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar candidaturas elegíveis para contratação.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void solicitarContratacao() {
        try {
            CandidaturaDTO selecao = (CandidaturaDTO) comboCandidaturas.getSelectedItem();
            if (selecao == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma candidatura para solicitar contratação.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            RegimeContratacao regime = (RegimeContratacao) comboRegime.getSelectedItem();
            if (regime == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecione o regime de contratação.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Regra de negócio do RecrutamentoService
            service.solicitarContratacao(selecao.getId(), regime);

            JOptionPane.showMessageDialog(this,
                    "Solicitação de contratação registrada com sucesso.",
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
                    "Erro inesperado ao solicitar a contratação.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
