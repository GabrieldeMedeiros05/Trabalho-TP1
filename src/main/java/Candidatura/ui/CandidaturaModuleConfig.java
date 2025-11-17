package Candidatura.ui;

import Candidatura.persistencia.CandidatoRepository;
import Candidatura.persistencia.CandidaturaRepository;
import Seguranca.persistencia.UsuarioRepository;
import Candidatura.persistencia.csv.CandidatoRepositoryCsv;
import Candidatura.persistencia.csv.CandidaturaRepositoryCsv;
import Seguranca.persistencia.csv.UsuarioRepositoryCsv;
import Candidatura.servico.CandidaturaService;
import Seguranca.servico.UsuarioService;

import java.nio.file.Path;

/**
 * Configuração simples do módulo — cria instâncias compartilhadas
 */
public class CandidaturaModuleConfig {

    private static final Path DATA_DIR = Path.of("data");

    // Repositórios
    private static final CandidaturaRepository candidaturaRepo =
            new CandidaturaRepositoryCsv(DATA_DIR.resolve("candidaturas.csv"));
    private static final CandidatoRepository candidatoRepo =
            new CandidatoRepositoryCsv(DATA_DIR.resolve("candidatos.csv"));
    private static final UsuarioRepository usuarioRepo =
            new UsuarioRepositoryCsv(DATA_DIR.resolve("usuarios.csv")); // NOVO

    // Serviços
    private static final CandidaturaService candidaturaService =
            new CandidaturaService(candidaturaRepo, candidatoRepo);
    private static final UsuarioService usuarioService =
            new UsuarioService(usuarioRepo); // NOVO

    public static CandidaturaService candidaturaService() { return candidaturaService; }
    public static UsuarioService usuarioService() { return usuarioService; }

    public static CandidaturaService service() {
        return null;
    }
}