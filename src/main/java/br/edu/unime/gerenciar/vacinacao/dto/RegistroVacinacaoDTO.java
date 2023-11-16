package br.edu.unime.gerenciar.vacinacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Data
@AllArgsConstructor //cria construtor com todos argumentos
@NoArgsConstructor //cria construtor sem argumentos
public class RegistroVacinacaoDTO {

    private Date dataVacinacao;
    private String idPaciente;
    private String idVacina;
    private int doses;
    private String estado;

    @CPF
    private String cpfProfisionalSaude;
}
