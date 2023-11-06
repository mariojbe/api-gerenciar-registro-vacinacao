package br.edu.unime.gerenciar.vacinacao.service.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
