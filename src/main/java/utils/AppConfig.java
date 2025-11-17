package utils;

import Seguranca.persistencia.UsuarioRepository;
import Seguranca.persistencia.csv.UsuarioRepositoryCsv;
import Seguranca.servico.UsuarioService;

import java.nio.file.Path;

/**
 * Configuração de todos os módulos da aplicação (incluindo Segurança).
 */
public class AppConfig {

    private static final Path DATA_DIR = Path.of("data");


    private static final UsuarioRepository usuarioRepo =
            new UsuarioRepositoryCsv(DATA_DIR.resolve("usuarios.csv"));

    private static final UsuarioService usuarioService =
            new UsuarioService(usuarioRepo);

    public static UsuarioService usuarioService() {
        return usuarioService;
    }
}