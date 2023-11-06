package br.edu.unime.gerenciar.vacinacao.repository;

import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import feign.Param;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegistroVacinacaoRepository extends MongoRepository<RegistroVacinacao, String> {

    public Optional<RegistroVacinacao> findById(String id);

    //public Optional<RegistroVacinacao> findByIdPaciente(@Param("idPaciente") String idPaciente);

    List<RegistroVacinacao> findByIdPaciente(String idPaciente);
}
