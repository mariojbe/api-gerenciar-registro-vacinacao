package br.edu.unime.gerenciar.vacinacao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor //cria construtor com todos argumentos
@NoArgsConstructor //cria construtor sem argumentos
@Document(collection = "vacinas")
public class RegistroVacinacao {
    @Id
    private String id;
}
