package recrutamento.integracao;

import Candidatura.dominio.Candidatura;
import Candidatura.persistencia.CandidaturaRepository;
import recrutamento.dto.CandidaturaDTO;
import recrutamento.interfaces.ICandidaturaService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter que converte chamadas do módulo Recrutamento
 * para o módulo Candidatura.
 */
public class CandidaturaServiceAdapter implements ICandidaturaService {

    private final CandidaturaRepository candidaturaRepository;

    public CandidaturaServiceAdapter(CandidaturaRepository candidaturaRepository) {
        this.candidaturaRepository = candidaturaRepository;
    }

    @Override
    public CandidaturaDTO buscarPorId(String candidaturaId) {
        return candidaturaRepository.buscarPorId(candidaturaId)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public List<CandidaturaDTO> listarPorVaga(String vagaId) {
        return candidaturaRepository.listarPorVaga(vagaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CandidaturaDTO toDto(Candidatura c) {
        return new CandidaturaDTO(
                c.getId(),
                c.getVagaId(),
                c.getCandidatoCpf(),
                c.getStatus().name() // converte enum para String
        );
    }
}
