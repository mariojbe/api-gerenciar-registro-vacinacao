package br.edu.unime.gerenciar.vacinacao.service;

import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import br.edu.unime.gerenciar.vacinacao.httpClient.PacienteHttpClient;
import br.edu.unime.gerenciar.vacinacao.repository.RegistroVacinacaoRepository;
import br.edu.unime.gerenciar.vacinacao.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegistroVacinacaoService {

    @Autowired
    RegistroVacinacaoRepository registroVacinacaoRepository;

    public List<RegistroVacinacao> obterTodos() {
        return registroVacinacaoRepository.findAll();
    }

    public RegistroVacinacao inserir(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> registrosDoPaciente = findByIdPaciente(registroVacinacao.getIdPaciente());
        List<RegistroVacinacao> vacinasComEsseId = new ArrayList<>();
        if (registrosDoPaciente.isEmpty()){
            registroVacinacaoRepository.insert(registroVacinacao);
        }else{
            for (RegistroVacinacao reg : registrosDoPaciente){
                  if (reg.getIdVacina() == registroVacinacao.getIdVacina() && reg.getFabricante() == registroVacinacao.getFabricante()){
                      vacinasComEsseId.add(reg);
                  }
            }

            if(vacinasComEsseId.isEmpty()){
                //se não encontrou nenhum registro com essa vacina então é a primeira dose dessa vacina em questão
                registroVacinacaoRepository.insert(registroVacinacao);
            }
           if ( vacinasComEsseId.size() < registroVacinacao.getDose()){
               for (RegistroVacinacao registroLocal : vacinasComEsseId){
                   if (registroLocal.getDataProximaVacinacao().after(registroVacinacao.getDataVacinacao()) || registroLocal.getDataProximaVacinacao().equals(registroVacinacao.getDataVacinacao()) ) {
                       registroVacinacaoRepository.insert(registroVacinacao);
                   }
               }
           }

        }
return registroVacinacao;
    }

    public RegistroVacinacao atualizarPorId(String id, RegistroVacinacao novosDadosDoRegistroVacinacao) {
        Optional<RegistroVacinacao> registroVacinacao = findById(id);

        if (registroVacinacao.isPresent()) {
            RegistroVacinacao novoPaciente = registroVacinacao.get();
            novoPaciente.setDataVacinacao(novosDadosDoRegistroVacinacao.getDataVacinacao());
            novoPaciente.setIdPaciente(novosDadosDoRegistroVacinacao.getIdPaciente());
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
        Optional<RegistroVacinacao> registroVacinacao = registroVacinacaoRepository.findById(id);

        RegistroVacinacao entity = registroVacinacao.orElseThrow(() -> new EntityNotFoundException("Registro Não Lacalizado!"));

        return Optional.ofNullable(entity);
    }

    public List<RegistroVacinacao> findByIdPaciente(String idPaciente) {
        try {
            List<RegistroVacinacao> registroVacinacao = registroVacinacaoRepository.findByIdPaciente(idPaciente);
        } catch (Exception e) {
            throw new ClassCastException("Registro Não Lacalizado!");
        }
        return registroVacinacaoRepository.findByIdPaciente(idPaciente);
    }

    public ResponseEntity<Map<String, Integer>> totalVacinasAplicadas() {
        Map<String, Integer> response = new HashMap();

        response.put("total_vacinas_aplicadas", (int) registroVacinacaoRepository.count());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Integer>> totalVacinasAplicadasPorEstado(String estado) {
        Map<String, Integer> response = new HashMap();

        response.put("total_vacinas_aplicadas", (int) registroVacinacaoRepository.count());

        return ResponseEntity.ok(response);
    }

}
