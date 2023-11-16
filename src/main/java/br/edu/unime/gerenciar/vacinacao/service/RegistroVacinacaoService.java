package br.edu.unime.gerenciar.vacinacao.service;

import br.edu.unime.gerenciar.vacinacao.MessagensErros.MensagensRetornoException;
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

    public List<RegistroVacinacao> registrosDessaVacinaNoPaciente(RegistroVacinacao registroVacinacao){
        List<RegistroVacinacao> vacinacoesDoPaciente = findByIdPaciente(registroVacinacao.getIdPaciente());
        List<RegistroVacinacao> vacinacoesComEssaVacina = new ArrayList<>();
        for (RegistroVacinacao reg : vacinacoesDoPaciente){
            if (reg.getIdVacina().equals(registroVacinacao.getIdVacina())  && reg.getFabricante().equals( registroVacinacao.getFabricante()) ){
                vacinacoesComEssaVacina.add(reg);
            }
        }
        return vacinacoesComEssaVacina;
    }
    Date dataPrevistaParaProximaVacina = new Date();
    public boolean verificarIntervaloEntreDoze(RegistroVacinacao registroVacinacao){
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
            for (RegistroVacinacao registroLocal : registrosDessaVacinaNoPaciente){
                dataPrevistaParaProximaVacina = registroLocal.getDataProximaVacinacao();
                if (registroLocal.getDataProximaVacinacao().before(registroVacinacao.getDataVacinacao()) || registroLocal.getDataProximaVacinacao().equals(registroVacinacao.getDataVacinacao()) ) {
                    return true;
                }
            }
        return false;
    }

    public boolean naoConcluiuTodasAsDozesParaEssaVacina(RegistroVacinacao registroVacinacao){
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
        if ( registrosDessaVacinaNoPaciente.size() == registroVacinacao.getDosesEspecificadas()){
           return false;
        }
        return true;
    }

    public ResponseEntity<Map<String, RegistroVacinacao>> inserir(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
        Map<String, RegistroVacinacao> response = new HashMap();
        if (registrosDessaVacinaNoPaciente.isEmpty()){
            registroVacinacaoRepository.insert(registroVacinacao);
        }else{
            if(naoConcluiuTodasAsDozesParaEssaVacina(registroVacinacao)){
                if(verificarIntervaloEntreDoze(registroVacinacao)){
                    registroVacinacaoRepository.insert(registroVacinacao);

                }else{
                    response.put("Intervalo entre doze menor que o especificado, previsão para: "+dataPrevistaParaProximaVacina, (RegistroVacinacao) registroVacinacao);
                    return ResponseEntity.badRequest().body(response);
                }
            }else{
                response.put("Todas as dozes para essa vacina ja foram tomadas.", (RegistroVacinacao) registroVacinacao);
               return ResponseEntity.badRequest().body(response);
            }
        }
        response.put("Registrado com sucesso!", (RegistroVacinacao) registroVacinacao);
        return ResponseEntity.ok().body(response);
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
