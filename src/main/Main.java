package main;

import javax.swing.SwingUtilities;
import main.ui.TelaLogin;
import Seguranca.persistencia.csv.PessoaRepositoryCsv;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> {
        //     TelaLogin telaLogin = new TelaLogin();
        //     telaLogin.setVisible(true);
        // });
        Path DATA_DIR = Path.of("data");
        PessoaRepositoryCsv candidaturaRepo = new PessoaRepositoryCsv(DATA_DIR.resolve("pessoas.csv"));
        // candidaturaRepo.salvar();
    }
}