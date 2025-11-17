package Financeiro.dados;

import Seguranca.dominio.Funcionario;
import Seguranca.dominio.Pessoa;
import utils.CsvParser;

import java.util.ArrayList;
import java.util.List;

public class LeitorFuncionario extends CsvParser implements RegistrosBase {

    private List<Funcionario> funcionarios = new ArrayList<Funcionario>();
    private List<Pessoa> pessoas = new LeitorPessoa().retornaLista();

    public LeitorFuncionario() {
        List<String[]> funcionariosRaw = super.read("data/funcionarios.csv");

        for (String[] funcionarioRaw: funcionariosRaw) {
            funcionarios.add(
                    new Funcionario(
                            Long.parseLong(funcionarioRaw[0]),
                            funcionarioRaw[1],
                            funcionarioRaw[2],
                            funcionarioRaw[3],
                            funcionarioRaw[4],
                            Float.parseFloat(funcionarioRaw[5]),
                            funcionarioRaw[6],
                            funcionarioRaw[7],
                            Integer.parseInt(funcionarioRaw[8]),
                            funcionarioRaw[9],
                            funcionarioRaw[10],
                            funcionarioRaw[11],
                            Boolean.parseBoolean(funcionarioRaw[12]),
                            Boolean.parseBoolean(funcionarioRaw[13]),
                            Boolean.parseBoolean(funcionarioRaw[14])

                )
            );
        }

        for (Funcionario funcionario: funcionarios) {
            for(Pessoa pessoa: pessoas) {
                if(funcionario.getIdPessoa() == pessoa.getId()) {
                    funcionario.setId(funcionario.getIdPessoa());
                    funcionario.setNome(pessoa.getNome());
                    funcionario.setFisicaOuJuridica(pessoa.getFisicaOuJuridica());
                    funcionario.setCpf_cnpj(pessoa.getCpf_cnpj());
                    funcionario.setDataNascimento(pessoa.getDataNascimento());
                    funcionario.setEmailPessoal(pessoa.getEmailPessoal());
                    funcionario.setTelefonePessoal(pessoa.getTelefonePessoal());
                    funcionario.setEnderecoCompleto(pessoa.getEnderecoCompleto());
                }
            }
        }


    }

    @Override
    public List<Funcionario> retornaLista() {
        return funcionarios;
    }

    public List<Funcionario> retornaListaAtivos() {
        return funcionarios.stream().filter(f -> f.getStatus().equalsIgnoreCase("Ativo")).toList();
    }
}
