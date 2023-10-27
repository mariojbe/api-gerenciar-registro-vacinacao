package br.edu.unime.gerenciar.vacinacao.entity;

import br.edu.unime.gerenciar.vacinacao.dto.RegistroVacinacaoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor //cria construtor com todos argumentos
@NoArgsConstructor //cria construtor sem argumentos
@Document(collection = "RegistroVacinacao")
public class RegistroVacinacao {
    @Id
    private String id;
    private Date DataVacinacao;
    private String IdPaciente;
    private String IdVacina;
    private int IdDose;

    private ProfisionalSaude profisionalSaude;
    //private String IdProfissionalSaude;

    public RegistroVacinacao(RegistroVacinacaoDTO registroVacinacaoDTO) {
        setDataVacinacao(registroVacinacaoDTO.getDataVacinacao());
        setIdPaciente(registroVacinacaoDTO.getIdPaciente());
        setIdVacina(registroVacinacaoDTO.getIdPaciente());
        setIdDose(registroVacinacaoDTO.getIdDose());
    }
}
