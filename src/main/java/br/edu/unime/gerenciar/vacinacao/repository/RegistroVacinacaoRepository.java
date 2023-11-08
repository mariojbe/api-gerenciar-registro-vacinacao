package br.edu.unime.gerenciar.vacinacao.repository;

import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import feign.Param;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistroVacinacaoRepository extends MongoRepository<RegistroVacinacao, String> {

    public Optional<RegistroVacinacao> findById(String id);

    public List<RegistroVacinacao> findByIdPaciente(String idPaciente);

}
