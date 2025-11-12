package Seguranca.persistencia.csv;

import Candidatura.excecoes.PersistenciaException;
import Seguranca.persistencia.PessoaRepository;
import Seguranca.dominio.Pessoa; // Seu domínio isolado

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistência CSV simples para Usuário (data/pessoas.csv).
 */
public class PessoaRepositoryCsv implements PessoaRepository {

    private final Path arquivo;
    private static final String CAB = "id;nome;personalidade;numeroRegistro;dataNascimento;emailPessoal;telefonePessoal;enderecoCompleto";
    // Número mínimo de campos esperados (idPessoa a tipo)
    private static final int MIN_CAMPOS_NECESSARIOS = 8;

    public PessoaRepositoryCsv(Path arquivo) {
        this.arquivo = arquivo;
        inicializar();
    }

    private void inicializar() {
        try {
            if (Files.notExists(arquivo.getParent())) {
                Files.createDirectories(arquivo.getParent());
            }
            if (Files.notExists(arquivo)) {
                Files.writeString(arquivo, CAB + System.lineSeparator());

                // CRIAÇÃO DE PESSOAS DE TESTE (COMPLETOS)
                Pessoa pessoaUm = new Pessoa(
                        51L, "Lívia Lima", "Física", "249.538.607-56", "1991-01-26", "gabriel21@example.net", "+55 (031) 5684 8154", "Campo Cassiano, 569 Vila Satélite 41185-168 Porto / MS"
                );
                salvar(pessoaUm);

                Pessoa pessoaDois = new Pessoa(
                        52L, "Miguel Souza", "Física", "753.124.089-05", "1980-04-27", "ecunha@example.org", "+55 (051) 0102-0840", "Recanto Oliveira, 6 Califórnia 87471752 Pimenta Alegre / DF"
                );
                salvar(pessoaDois);
            }
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao inicializar arquivo de pessoas", e);
        }
    }

    @Override
    public Pessoa salvar(Pessoa p) {
        List<Pessoa> todos = listarTodos();

        todos.removeIf(x -> x.getId() == p.getId());

        if (p.getId() == 0) {
            long novoId = todos.stream().mapToLong(Pessoa::getId).max().orElse(0) + 1;
            p.setId(novoId);
        }
        todos.add(p);
        escreverTodos(todos);
        return p;
    }

//    @Override
//    public Optional<Pessoa> buscarPorId(String id) {
//        return listarTodos()
//                .stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst();
//    }

    @Override
    public Optional<Pessoa> buscarPorCpf(String cpf) {
        return listarTodos()
                .stream()
                .filter(p -> p.getNumeroRegistro() != null && p.getNumeroRegistro().equals(cpf))
                .findFirst();
    }

    // @Override
    // public Optional<Pessoa> buscarPorCargo(String cargo) {
    //     return listarTodos()
    //             .stream()
    //             .filter(p -> p.getNumeroRegistro() != null && u.getNumeroRegistro().equals(cpf))
    //             .findFirst();
    // }

    // @Override
    // public Optional<Pessoa> buscarPorTipoContratacao(String tipoContratacao) {
    //     return listarTodos()
    //             .stream()
    //             .filter(p -> p.getNumeroRegistro() != null && u.getNumeroRegistro().equals(cpf))
    //             .findFirst();
    // }

    // @Override
    // public Optional<Pessoa> buscarPorStatus(String status) {
    //     return listarTodos()
    //             .stream()
    //             .filter(p -> p.getNumeroRegistro() != null && u.getNumeroRegistro().equals(cpf))
    //             .findFirst();
    // }

    @Override
    public List<Pessoa> listarTodos() {
        try (BufferedReader br = Files.newBufferedReader(arquivo)) {
            return br.lines()
                    .skip(1)
                    .filter(l -> !l.isBlank())
                    .map(this::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo usuários", e);
        }
    }


    private Pessoa parse(String l) {
        try {
            String[] t = l.split(";", -1);

            if (t.length < MIN_CAMPOS_NECESSARIOS) {
                System.err.println("Linha incompleta no CSV de usuários: " + l);
                return null;
            }

            // Atributos de Pessoa
            long id = Long.parseLong(t[0]);
            String nome = t[1];
            String personalidade = t[2];
            String numeroRegistro = t[3];
            String dataNascimento = t[4];
            String emailPessoal = t[5];
            String telefonePessoal = t[6];
            String enderecoCompleto = t[7];

            // Cria o usuário usando o construtor completo
            return new Pessoa(
                    id,
                    nome,
                    personalidade,
                    numeroRegistro,
                    dataNascimento,
                    emailPessoal,
                    telefonePessoal,
                    enderecoCompleto
            );

        } catch (Exception e) {
            System.err.println("Erro processando linha: " + l + ". Detalhe: " + e.getMessage());
            return null;
        }
    }

    private void escreverTodos(List<Pessoa> todos) {
        try (BufferedWriter bw = Files.newBufferedWriter(arquivo, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            bw.write(CAB);
            bw.newLine();
            for (Pessoa p : todos) {
                bw.write(String.join(";", List.of(
                        String.valueOf(p.getId()),
                        p.getNome(),
                        p.getNumeroRegistro(),
                        p.getDataNascimento(),
                        p.getEmailPessoal(),
                        p.getTelefonePessoal(),
                        p.getEnderecoCompleto()
                )));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro escrevendo pessoas", e);
        }
    }
}