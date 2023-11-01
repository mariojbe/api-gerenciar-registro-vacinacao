package br.edu.unime.gerenciar.vacinacao.repository;

import br.edu.unime.gerenciar.vacinacao.entity.ProfissionalSaude;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfissionalSaudeRepository extends MongoRepository<ProfissionalSaude, String> {

    public Optional<ProfissionalSaude> findById(String id);

    //public Optional<ProfissionalSaude> findByCpf(String cpf);

    ProfissionalSaude findByCpf(String cpf);

}
