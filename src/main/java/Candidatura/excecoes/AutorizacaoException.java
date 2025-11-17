package Candidatura.excecoes;

public class AutorizacaoException extends RuntimeException {
    public AutorizacaoException(String msg) { super(msg); }
    public AutorizacaoException(String msg, Throwable t) { super(msg, t); }
}
