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

    public ProfissionalSaude obterProfissionalSaudePorCPF(String cpf) throws Exception {
        ProfissionalSaude profissionalSaude = profissionalSaudeRepository.findByCpf(cpf);

        if (profissionalSaude == null) {
            throw new Exception("Profissional de Saúde não encontrado!");
        }

        return profissionalSaude;
    }

    public void inserir(ProfissionalSaude profissionalSaude) {
        profissionalSaudeRepository.insert(profissionalSaude);
    }

    public ProfissionalSaude atualizarPorId(String id, ProfissionalSaude novosDadosDoProfissionalSaude) {
        Optional<ProfissionalSaude> profissionalSaude = findById(id);

        if (profissionalSaude.isPresent()) {
            ProfissionalSaude novoProfissionalSaude = profissionalSaude.get();
            novoProfissionalSaude.setNome(novosDadosDoProfissionalSaude.getNome());
            novoProfissionalSaude.setCpf(novosDadosDoProfissionalSaude.getCpf());
            profissionalSaudeRepository.save(novoProfissionalSaude);
            return novoProfissionalSaude;
        }
        return null;

    }

    public void remove(String id) {
        Optional<ProfissionalSaude> profissionalSaude = findById(id);

        profissionalSaude.ifPresent(value -> profissionalSaudeRepository.delete(value));
    }

    public Optional<ProfissionalSaude> findById(String id) {
        Optional<ProfissionalSaude> profissionalSaude = profissionalSaudeRepository.findById(id);

        ProfissionalSaude entity = profissionalSaude.orElseThrow(() -> new EntityNotFoundException("Paciente Não Lacalizado!"));

        return Optional.ofNullable(entity);
    }

}
