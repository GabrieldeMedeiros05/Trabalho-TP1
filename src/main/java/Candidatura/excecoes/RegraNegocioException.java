package Candidatura.excecoes;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String msg) { super(msg); }
    public RegraNegocioException(String msg, Throwable t) { super(msg, t); }
}
