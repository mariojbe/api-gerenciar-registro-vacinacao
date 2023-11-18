package br.edu.unime.gerenciar.vacinacao.entity;

import br.edu.unime.gerenciar.vacinacao.dto.RegistroVacinacaoDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
    @NotNull
    @Past // deve ser obrigatorio e a data deve ser atual
    private Date dataVacinacao;
    private String idPaciente;
    private String nomePaciente;
    private String nomeVacina;
    private String idVacina;
    private int dosesEspecificadas;
    private String estado;
    private String fabricante;
    @NotNull
    @Future(message = "A data de validade deve ser futura!")
    private Date dataProximaVacinacao;

    private ProfissionalSaude profissionalSaude;

    public RegistroVacinacao(RegistroVacinacaoDTO registroVacinacaoDTO) {
        setDataVacinacao(registroVacinacaoDTO.getDataVacinacao());
        setIdPaciente(registroVacinacaoDTO.getIdPaciente());
        setIdVacina(registroVacinacaoDTO.getIdPaciente());
        setDosesEspecificadas(registroVacinacaoDTO.getDoses());
    }
}
