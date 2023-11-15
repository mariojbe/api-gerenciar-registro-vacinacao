package br.edu.unime.gerenciar.vacinacao.entity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor //cria construtor com todos argumentos
@NoArgsConstructor //cria construtor sem argumentos
//Expressão em JavaScript que será executada durante a validação
//@ScriptAssert(lang = "javascript", script = "_this.validade.isAfter(LocalDateTime.now())", message = "A data de validade deve ser futura")
public class Vacina {
    @Id
    private String id;
    @NotNull
    @Indexed(unique = true)
    private int codigo;
    @NotBlank(message = "O nome da vacina não foi informado!")
    private String nome;
    @NotBlank(message = "O nome do fabricante não foi informado!")
    private String fabricante;
    @NotNull
    @Min(value = 1, message = "Informe um número maior que 0!")
    private String lote;
    @NotNull
    @Future(message = "A data de validade deve ser futura!")
    private Date validade;
    @NotNull
    @Min(value = 1, message = "Informe um número maior que 0!")
    private int doses; // Número de doses
    private int intervaloEntreDoses;// Intervalo mínimo entre doses em dias
}