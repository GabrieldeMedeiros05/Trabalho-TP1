package recrutamento.integracao;

import Candidatura.dominio.Candidatura;
import Candidatura.persistencia.CandidaturaRepository;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.interfaces.ICandidaturaService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter entre o módulo Recrutamento e o módulo Candidatura.
 * Converte o domínio Candidatura em CandidaturaDTO para uso no Recrutamento.
 */
public class CandidaturaServiceAdapter implements ICandidaturaService {

    private final CandidaturaRepository candidaturaRepository;

    public CandidaturaServiceAdapter(CandidaturaRepository candidaturaRepository) {
        this.candidaturaRepository = candidaturaRepository;
    }

    private CandidaturaDTO toDTO(Candidatura c) {
        if (c == null) return null;
        return new CandidaturaDTO(
                c.getId(),
                c.getVagaId(),
                c.getCandidatoCpf(),
                c.getStatus().name() // enum -> String
        );
    }

    @Override
    public CandidaturaDTO buscarPorId(String candidaturaId) {
        return candidaturaRepository.buscarPorId(candidaturaId)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public List<CandidaturaDTO> listarPorVaga(String vagaId) {
        return candidaturaRepository.listarPorVaga(vagaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CandidaturaDTO> listarTodas() {
        // Certifique-se de que o CandidaturaRepository tem listarTodos().
        // Se o nome for diferente, ajuste aqui.
        return candidaturaRepository.listarTodas().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
