package br.edu.unime.gerenciar.vacinacao.httpClient;

import br.edu.unime.gerenciar.vacinacao.entity.Paciente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "paciente-client", url = "https://api-pacientes-af7078c573aa.herokuapp.com/api/pacientes")
public interface PacienteHttpClient {

    @GetMapping("/{id}")
    public Paciente obterPacientePorId(@PathVariable("id") String id);

    @GetMapping("/uf/{estado}")
    public Paciente obterPacientePorEstado(@PathVariable("estado") String estado);

}
