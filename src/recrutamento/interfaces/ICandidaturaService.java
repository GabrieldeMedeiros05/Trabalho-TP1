package recrutamento.interfaces;

import recrutamento.dto.CandidaturaDTO;

import java.util.List;

public interface ICandidaturaService {

    CandidaturaDTO buscarPorId(String candidaturaId);

    List<CandidaturaDTO> listarPorVaga(String vagaId);

    // Novo metodo para apoiar telas como TelaMarcarEntrevista
    List<CandidaturaDTO> listarTodas();
}
