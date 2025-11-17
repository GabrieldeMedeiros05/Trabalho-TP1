package Candidatura.excecoes;

public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String msg) { super(msg); }
    public PersistenciaException(String msg, Throwable t) { super(msg, t); }
}
