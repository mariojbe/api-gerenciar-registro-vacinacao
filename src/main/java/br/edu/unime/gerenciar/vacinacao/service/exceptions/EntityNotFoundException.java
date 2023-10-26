package br.edu.unime.gerenciar.vacinacao.service.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg) {
        super(msg);
    }

}
