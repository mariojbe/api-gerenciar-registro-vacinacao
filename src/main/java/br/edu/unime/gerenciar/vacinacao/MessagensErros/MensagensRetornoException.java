package br.edu.unime.gerenciar.vacinacao.MessagensErros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MensagensRetornoException extends RuntimeException{
    public MensagensRetornoException(String message){
        super(message);
    }
}
