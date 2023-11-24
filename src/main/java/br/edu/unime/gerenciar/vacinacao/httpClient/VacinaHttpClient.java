package br.edu.unime.gerenciar.vacinacao.httpClient;

import br.edu.unime.gerenciar.vacinacao.entity.Vacina;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "vacina-client", url = "https://api-vacinas-bsi-f03cac251e91.herokuapp.com/api/vacinas")
public interface VacinaHttpClient {
    @GetMapping("/{id}")
    public Vacina obterVacinaPorId(@PathVariable("id") String id);
}