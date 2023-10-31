package br.edu.unime.gerenciar.vacinacao.service;

import br.edu.unime.gerenciar.vacinacao.entity.ProfissionalSaude;
import br.edu.unime.gerenciar.vacinacao.repository.ProfissionalSaudeRepository;
import br.edu.unime.gerenciar.vacinacao.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalSaudeService {

    @Autowired
    ProfissionalSaudeRepository profissionalSaudeRepository;

    public List<ProfissionalSaude> obterTodos() {
        return profissionalSaudeRepository.findAll();
    }

    public void inserir(ProfissionalSaude profissionalSaude) {
        profissionalSaudeRepository.insert(profissionalSaude);
    }

    public ProfissionalSaude atualizarPorId(String id, ProfissionalSaude novosDadosDoProfissionalSaude) {
        Optional<ProfissionalSaude> profissionalSaude = findById(id);

        if (profissionalSaude.isPresent()) {
            ProfissionalSaude novoPaciente = profissionalSaude.get();
            novoPaciente.setNome(novosDadosDoProfissionalSaude.getNome());
            novoPaciente.setCpf(novosDadosDoProfissionalSaude.getCpf());
            profissionalSaudeRepository.save(novoPaciente);
            return novoPaciente;
        }
        return null;

    }

    public void remove(String id) {
        Optional<ProfissionalSaude> profissionalSaude = findById(id);

        profissionalSaude.ifPresent(value -> profissionalSaudeRepository.delete(value));
    }

    public Optional<ProfissionalSaude> findById(String id) {
        Optional<ProfissionalSaude> paciente = profissionalSaudeRepository.findById(id);

        ProfissionalSaude entity = paciente.orElseThrow(() -> new EntityNotFoundException("Paciente Não Lacalizado!"));

        return Optional.ofNullable(entity);
    }

}
