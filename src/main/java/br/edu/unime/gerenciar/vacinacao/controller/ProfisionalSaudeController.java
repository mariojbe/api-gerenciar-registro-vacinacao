package br.edu.unime.gerenciar.vacinacao.controller;

import br.edu.unime.gerenciar.vacinacao.entity.ProfissionalSaude;
import br.edu.unime.gerenciar.vacinacao.service.ProfissionalSaudeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/profsaude")
public class ProfisionalSaudeController {

    @Autowired
    ProfissionalSaudeService profissionalSaudeService;

    @GetMapping
    public ResponseEntity<List<ProfissionalSaude>> obterTodos() {
        return ResponseEntity.ok().body(profissionalSaudeService.obterTodos());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<?> obterProfissionalSaudePorCPF(@PathVariable String cpf) {
        try {
            ProfissionalSaude profissionalSaude = profissionalSaudeService.obterProfissionalSaudePorCPF(cpf);

            return ResponseEntity.ok().body(profissionalSaude);
        } catch (Exception e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);

        }
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<ProfissionalSaude> obterVacinaPorId(@PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profissionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(profissionalSaude.get());
    }


    @PostMapping
    public ResponseEntity<ProfissionalSaude> inserir(@RequestBody @Valid ProfissionalSaude profissionalSaude) {
        profissionalSaudeService.inserir(profissionalSaude);
        return ResponseEntity.created(null).body(profissionalSaude);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProfissionalSaude> atualizarPorId(@RequestBody @Valid ProfissionalSaude
                                                                    novosDadosDoProfissionalSaude, @PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profissionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProfissionalSaude responseVacina = profissionalSaudeService.atualizarPorId(id, novosDadosDoProfissionalSaude);
        return ResponseEntity.ok().body(responseVacina);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProfissionalSaude> remover(@PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profissionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        profissionalSaudeService.remove(id);

        return ResponseEntity.ok().body(null);
    }


}
