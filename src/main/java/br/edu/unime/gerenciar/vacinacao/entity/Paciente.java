package br.edu.unime.gerenciar.vacinacao.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 100, message = "o nome deve ter entre 3 e 100 digitos!")
    private String nome;
    private String sobrenome;

    @CPF
    @Indexed(unique = true)
    private String cpf;

    @NotNull
    @Min(value = 1, message = "Informe uma idade maior que 0!")
    private int idade;

    private String sexo;
    private String contato;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cep;
    private String municipio;
    private String estado;

    /*private Endereco endereco;

    public Paciente(PacienteDTO pacienteDTO) {
        setNome(pacienteDTO.getNome());
        setSobrenome(pacienteDTO.getSobrenome());
        setCpf(pacienteDTO.getCpf());
        setIdade(pacienteDTO.getIdade());
    }*/

}
