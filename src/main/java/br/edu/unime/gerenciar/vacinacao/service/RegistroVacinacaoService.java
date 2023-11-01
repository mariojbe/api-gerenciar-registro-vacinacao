package br.edu.unime.gerenciar.vacinacao.service;

import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import br.edu.unime.gerenciar.vacinacao.repository.RegistroVacinacaoRepository;
import br.edu.unime.gerenciar.vacinacao.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroVacinacaoService {

    @Autowired
    RegistroVacinacaoRepository registroVacinacaoRepository;

    public List<RegistroVacinacao> obterTodos() {
        return registroVacinacaoRepository.findAll();
    }

    public void inserir(RegistroVacinacao registroVacinacao) {
        registroVacinacaoRepository.insert(registroVacinacao);
    }

    public RegistroVacinacao atualizarPorId(String id, RegistroVacinacao novosDadosDoRegistroVacinacao) {
        Optional<RegistroVacinacao> registroVacinacao = findById(id);

        if (registroVacinacao.isPresent()) {
            RegistroVacinacao novoPaciente = registroVacinacao.get();
            novoPaciente.setDataVacinacao(novosDadosDoRegistroVacinacao.getDataVacinacao());
            novoPaciente.setIdPaciente(novosDadosDoRegistroVacinacao.getIdPaciente());
            novoPaciente.setIdVacina(novosDadosDoRegistroVacinacao.getIdVacina());
            novoPaciente.setIdVacina(novosDadosDoRegistroVacinacao.getIdVacina());
            novoPaciente.setProfissionalSaude(novosDadosDoRegistroVacinacao.getProfissionalSaude());
            registroVacinacaoRepository.save(novoPaciente);
            return novoPaciente;
        }
        return null;

    }

    public void remove(String id) {
        Optional<RegistroVacinacao> paciente = findById(id);

        paciente.ifPresent(value -> registroVacinacaoRepository.delete(value));
    }

    public Optional<RegistroVacinacao> findById(String id) {
        Optional<RegistroVacinacao> paciente = registroVacinacaoRepository.findById(id);

        RegistroVacinacao entity = paciente.orElseThrow(() -> new EntityNotFoundException("Paciente Não Lacalizado!"));

        return Optional.ofNullable(entity);
    }


}
