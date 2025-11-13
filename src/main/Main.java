package main;

import javax.swing.*;

import Financeiro.dados.LeitorPessoa;
import Financeiro.dados.LeitorFuncionario;
import Financeiro.ui.TelaFinanceiro;
import Financeiro.ui.TelaFolhaPagamento;
import Seguranca.dominio.Funcionario;
import Seguranca.dominio.Pessoa;
import main.ui.TelaLogin;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Funcionario> funcionarios = new LeitorFuncionario().retornaLista();
        for (Pessoa funcionario: funcionarios) {
            System.out.println(funcionario);
        }

        JFrame tela = new TelaFinanceiro();
        tela.setVisible(true);
//        SwingUtilities.invokeLater(() -> {
//            TelaLogin telaLogin = new TelaLogin();
//            telaLogin.setVisible(true);
//        });
    }
}