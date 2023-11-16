package br.edu.unime.gerenciar.vacinacao.controller;

import br.edu.unime.gerenciar.vacinacao.dto.RegistroVacinacaoDTO;
import br.edu.unime.gerenciar.vacinacao.entity.Paciente;
import br.edu.unime.gerenciar.vacinacao.entity.ProfissionalSaude;
import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import br.edu.unime.gerenciar.vacinacao.entity.Vacina;
import br.edu.unime.gerenciar.vacinacao.httpClient.PacienteHttpClient;
import br.edu.unime.gerenciar.vacinacao.httpClient.VacinaHttpClient;
import br.edu.unime.gerenciar.vacinacao.service.ProfissionalSaudeService;
import br.edu.unime.gerenciar.vacinacao.service.RegistroVacinacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/vacinacao")
public class RegistroVacinacaoController {

    @Autowired
    VacinaHttpClient vacinaHttpClient;

    @Autowired
    PacienteHttpClient pacienteHttpClient;

    @Autowired
    RegistroVacinacaoService registroVacinacaoService;

    @Autowired
    ProfissionalSaudeService profissionalSaudeService;

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

    @GetMapping
    public List<RegistroVacinacao> obterTodos() {
        return registroVacinacaoService.obterTodos();
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<RegistroVacinacao> obterRegistroVacinacaoPorId(@PathVariable String id) {
        Optional<RegistroVacinacao> registroVacinacao = registroVacinacaoService.findById(id);

        if (registroVacinacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(registroVacinacao.get());
    }

    @GetMapping("/obter/paciente/{idPaciente}")
    public List<RegistroVacinacao> obterRegistroVacinacaoPorIdPaciente(@PathVariable String idPaciente) {
        List<RegistroVacinacao> registroVacinacao = registroVacinacaoService.findByIdPaciente(idPaciente);

        if (registroVacinacao.isEmpty()) {
            return (List<RegistroVacinacao>) ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(registroVacinacao).getBody();
        //return registroVacinacao;
    }
    Date GetDataProximaVacinacao(int dias){
        Date d = new Date();
        d.setDate(d.getDate() + dias);
        return d;
    }
    @PostMapping("/cadastrar")
    public ResponseEntity<Map<String, RegistroVacinacao>> inserir(@RequestBody @Valid RegistroVacinacaoDTO registroVacinacaoDTO) throws Exception {
        RegistroVacinacao registroVacinacao = new RegistroVacinacao(registroVacinacaoDTO);
        Date dataAtual = new Date();


        Paciente paciente = pacienteHttpClient.obterPacientePorId(registroVacinacaoDTO.getIdPaciente());
        Vacina vacina = vacinaHttpClient.obterVacinaPorId(registroVacinacaoDTO.getIdVacina());
        ProfissionalSaude profissionalSaude = profissionalSaudeService.obterProfissionalSaudePorCPF(registroVacinacaoDTO.getCpfProfisionalSaude());

        registroVacinacao.setEstado(paciente.getEstado());
        registroVacinacao.setFabricante(vacina.getFabricante());
        registroVacinacao.setIdPaciente(paciente.getId());
        registroVacinacao.setIdVacina(vacina.getId());
        registroVacinacao.setProfissionalSaude(profissionalSaude);
        registroVacinacao.setDataProximaVacinacao(GetDataProximaVacinacao(vacina.getIntervaloEntreDoses()));
        registroVacinacao.setDataVacinacao(dataAtual);
        registroVacinacao.setDosesEspecificadas(vacina.getDoses());
        registroVacinacao.setEstado(registroVacinacao.getEstado());

        return registroVacinacaoService.inserir(registroVacinacao);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<RegistroVacinacao> atualizarPorId(@RequestBody RegistroVacinacao novosDadosDoPRegistroVacinacao, @PathVariable String id) {
        Optional<RegistroVacinacao> registroVacinacao = registroVacinacaoService.findById(id);

        if (registroVacinacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RegistroVacinacao responseRegistroVacinacao = registroVacinacaoService.atualizarPorId(id, novosDadosDoPRegistroVacinacao);
        return ResponseEntity.ok().body(responseRegistroVacinacao);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<RegistroVacinacao> remover(@PathVariable String id) {
        Optional<RegistroVacinacao> registroVacinacao = registroVacinacaoService.findById(id);

        if (registroVacinacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        registroVacinacaoService.remove(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/vacinas-aplicadas")
    public ResponseEntity<Map<String, Integer>> getTotalVacinasAplicadas() {
        return registroVacinacaoService.totalVacinasAplicadas();
    }
    
}
