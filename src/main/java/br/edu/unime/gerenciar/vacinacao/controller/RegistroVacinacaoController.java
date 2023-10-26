package br.edu.unime.gerenciar.vacinacao.controller;

import br.edu.unime.gerenciar.vacinacao.entity.Paciente;
import br.edu.unime.gerenciar.vacinacao.entity.Vacina;
import br.edu.unime.gerenciar.vacinacao.httpClient.PacienteHttpClient;
import br.edu.unime.gerenciar.vacinacao.httpClient.VacinaHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/vacinacao")
public class RegistroVacinacaoController {

    @Autowired
    VacinaHttpClient vacinaHttpClient;

    @Autowired
    PacienteHttpClient pacienteHttpClient;

    // http://localhost:8080/api/vacinacao/vacina
    @GetMapping("/vacina")
    public Vacina obterVacina() {
        Vacina vacina = vacinaHttpClient.obterVacinaPorId("6529f2fe91cd2c0970b33adc");

        return vacina;
    }

    // http://localhost:8080/api/vacinacao/paciente
    @GetMapping("/paciente")
    public Paciente obterPaciente() {
        Paciente paciente = pacienteHttpClient.obterPacientePorId("65308e95efd6ad6c718ae91d");

        return paciente;
    }


}
