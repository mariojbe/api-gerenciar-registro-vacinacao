package br.edu.unime.gerenciar.vacinacao.controller;

import br.edu.unime.gerenciar.vacinacao.entity.ProfissionalSaude;
import br.edu.unime.gerenciar.vacinacao.service.ProfissionalSaudeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/profsaude")
public class ProfisionalSaudeController {

    @Autowired
    ProfissionalSaudeService profisionalSaudeService;

    @GetMapping
    public ResponseEntity<List<ProfissionalSaude>> obterTodos() {
        return ResponseEntity.ok().body(profisionalSaudeService.obterTodos());
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<ProfissionalSaude> obterVacinaPorId(@PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profisionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(profissionalSaude.get());
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<ProfissionalSaude> inserir(@RequestBody @Valid ProfissionalSaude profissionalSaude) {
        profisionalSaudeService.inserir(profissionalSaude);
        return ResponseEntity.created(null).body(profissionalSaude);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<ProfissionalSaude> atualizarPorId(@RequestBody ProfissionalSaude novosDadosDoProfissionalSaude, @PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profisionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProfissionalSaude responseVacina = profisionalSaudeService.atualizarPorId(id, novosDadosDoProfissionalSaude);
        return ResponseEntity.ok().body(responseVacina);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<ProfissionalSaude> remover(@PathVariable String id) {
        Optional<ProfissionalSaude> profissionalSaude = profisionalSaudeService.findById(id);

        if (profissionalSaude.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        profisionalSaudeService.remove(id);

        return ResponseEntity.ok().body(null);
    }


}
