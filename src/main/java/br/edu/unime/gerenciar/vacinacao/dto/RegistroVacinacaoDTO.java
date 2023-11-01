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

    private Date DataVacinacao;
    private String IdPaciente;
    private String IdVacina;
    private int IdDose;

    @CPF
    private String cpfProfisionalSaude;

    //private String IdProfissionalSaude;
}
