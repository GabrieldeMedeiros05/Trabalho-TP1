package Financeiro.dados;

import Seguranca.dominio.Pessoa;
import utils.CsvParser;

import java.util.ArrayList;
import java.util.List;

public class LeitorPessoa extends CsvParser implements RegistrosBase {

    private List<Pessoa> pessoas = new ArrayList<Pessoa>();

    public LeitorPessoa() {
        List<String[]> pessoasRaw = super.read("data/pessoas.csv");

        for (String[] pessoaRaw: pessoasRaw) {
            pessoas.add(new Pessoa(Long.parseLong(pessoaRaw[0]), pessoaRaw[1], pessoaRaw[2], pessoaRaw[3], pessoaRaw[4], pessoaRaw[5], pessoaRaw[6], pessoaRaw[7]));
        }
    }

    @Override
    public List<Pessoa> retornaLista() {
        return pessoas;
    }
}
