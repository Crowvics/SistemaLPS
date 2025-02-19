package model.exception;

public class LoginException extends RuntimeException {

    public LoginException(String mensagem) {
        super(mensagem);
    }
}
