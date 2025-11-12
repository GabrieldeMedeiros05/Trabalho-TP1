package Seguranca.persistencia;

import Seguranca.dominio.Pessoa; // NOVO IMPORT

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {
    Pessoa salvar(Pessoa pessoa);
//    Optional<Pessoa> buscarPorId(String id);
    Optional<Pessoa> buscarPorCpf(String cpf);
    // Optional<Pessoa> buscarPorCargo(String cargo);
    // Optional<Pessoa> buscarPorTipoContratacao(String tipoContratacao);
    // Optional<Pessoa> buscarPorStatus(String status);
    List<Pessoa> listarTodos();
}