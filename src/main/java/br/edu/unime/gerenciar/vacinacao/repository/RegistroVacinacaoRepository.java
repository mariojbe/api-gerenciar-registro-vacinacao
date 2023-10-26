package br.edu.unime.gerenciar.vacinacao.repository;

import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RegistroVacinacaoRepository extends MongoRepository<RegistroVacinacao, String> {

    public Optional<RegistroVacinacao> findById(String id);

}
