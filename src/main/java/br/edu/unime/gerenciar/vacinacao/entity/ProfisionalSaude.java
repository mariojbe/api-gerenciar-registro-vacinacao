package br.edu.unime.gerenciar.vacinacao.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor //cria construtor com todos argumentos
@NoArgsConstructor //cria construtor sem argumentos
public class ProfisionalSaude {
    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 100, message = "o nome deve ter entre 3 e 100 digitos!")
    private String Nome;

    @CPF
    @Indexed(unique = true)
    private String Cpf;
}
