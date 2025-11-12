package main;

import javax.swing.*;

import Financeiro.ui.TelaFinanceiro;
import Financeiro.ui.TelaFolhaPagamento;
import main.ui.TelaLogin;

public class Main {
    public static void main(String[] args) {
        JFrame tela = new TelaFolhaPagamento();
        tela.setVisible(true);
//        SwingUtilities.invokeLater(() -> {
//            TelaLogin telaLogin = new TelaLogin();
//            telaLogin.setVisible(true);
//        });
    }
}