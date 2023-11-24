package br.edu.unime.gerenciar.vacinacao.service;

import br.edu.unime.gerenciar.vacinacao.MessagensErros.MensagensRetornoException;
import br.edu.unime.gerenciar.vacinacao.entity.RegistroVacinacao;
import br.edu.unime.gerenciar.vacinacao.httpClient.PacienteHttpClient;
import br.edu.unime.gerenciar.vacinacao.repository.RegistroVacinacaoRepository;
import br.edu.unime.gerenciar.vacinacao.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class RegistroVacinacaoService {

    @Autowired
    RegistroVacinacaoRepository registroVacinacaoRepository;

    public List<RegistroVacinacao> obterTodos() {
        return registroVacinacaoRepository.findAll();
    }

    public List<RegistroVacinacao> registrosDessaVacinaNoPaciente(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> vacinacoesDoPaciente = findByIdPaciente(registroVacinacao.getIdPaciente());
        List<RegistroVacinacao> vacinacoesComEssaVacina = new ArrayList<>();
        for (RegistroVacinacao reg : vacinacoesDoPaciente) {
            if (reg.getIdVacina().equals(registroVacinacao.getIdVacina()) && reg.getFabricante().equals(registroVacinacao.getFabricante())) {
                vacinacoesComEssaVacina.add(reg);
            }
        }
        return vacinacoesComEssaVacina;
    }

    Date dataPrevistaParaProximaVacina = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public RegistroVacinacao verificarIntervaloEntreDoze(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
        RegistroVacinacao registroRetorno = new RegistroVacinacao();
        for (RegistroVacinacao registroLocal : registrosDessaVacinaNoPaciente) {
            dataPrevistaParaProximaVacina = registroLocal.getDataProximaVacinacao();
            if (registroLocal.getDataProximaVacinacao().before(registroVacinacao.getDataVacinacao()) || registroLocal.getDataProximaVacinacao().equals(registroVacinacao.getDataVacinacao())) {
                return registroLocal;
            } else {
                if (registroRetorno.getId() == null) {
                    registroRetorno = registroLocal;
                } else if (registroLocal.getDataVacinacao().after(registroRetorno.getDataVacinacao())) {
                    registroRetorno = registroLocal;
                }
            }
        }
        return registroRetorno;
    }

    public RegistroVacinacao verificarSeTemRegistroDeOutroFabricanteEVacina(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> vacinacoesDoPaciente = findByIdPaciente(registroVacinacao.getIdPaciente());
        RegistroVacinacao vacinacoesDiferente = new RegistroVacinacao();
        for (RegistroVacinacao vacinacao : vacinacoesDoPaciente) {
            if (!vacinacao.getIdVacina().equals(registroVacinacao.getIdVacina())) {
                vacinacoesDiferente = vacinacao;
            }
        }
        return vacinacoesDiferente;
    }


    public RegistroVacinacao naoConcluiuTodasAsDozesParaEssaVacina(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
        RegistroVacinacao ultimoRegistro = new RegistroVacinacao();

        if (registrosDessaVacinaNoPaciente.size() == registroVacinacao.getDosesEspecificadas()) {

            for (RegistroVacinacao vacinacao : registrosDessaVacinaNoPaciente) {
                if (ultimoRegistro.getId() == null) {
                    ultimoRegistro = vacinacao;
                }

                if (ultimoRegistro.getDataVacinacao().before(vacinacao.getDataVacinacao())) {
                    ultimoRegistro = vacinacao;
                }
            }
        }
        return ultimoRegistro;
    }

    public ResponseEntity<?> inserir(RegistroVacinacao registroVacinacao) {
        List<RegistroVacinacao> registrosDessaVacinaNoPaciente = registrosDessaVacinaNoPaciente(registroVacinacao);
        String menssagemErro = "";
        RegistroVacinacao registroVacinaDiferente = verificarSeTemRegistroDeOutroFabricanteEVacina(registroVacinacao);

        if (registrosDessaVacinaNoPaciente.isEmpty() && registroVacinaDiferente.getId() == null) {
            registroVacinacaoRepository.insert(registroVacinacao);
        } else {
            RegistroVacinacao registroAtual = naoConcluiuTodasAsDozesParaEssaVacina(registroVacinacao);
            if (registroAtual.getId() == null) {
                RegistroVacinacao registroV = verificarIntervaloEntreDoze(registroVacinacao);
                if (registroV.getId() != null) {
                    if (registroV.getId().equals(registroVacinacao.getId()) && registroVacinaDiferente.getId() == null) {
                        registroVacinacaoRepository.insert(registroVacinacao);
                    } else if (registroVacinaDiferente.getId() != null) {
                        menssagemErro ="A primeira dose aplicada no paciente " + registroVacinaDiferente.getNomePaciente() + " foi:" + registroVacinaDiferente.getNomeVacina() + " do laboratório " + registroVacinaDiferente.getFabricante() + ". Todas as doses devem ser aplicadas com " + "o mesmo medicamento!";
                        return ResponseEntity.badRequest().body(menssagemErro);
                    } else {
                        menssagemErro = "O paciente " + registroV.getNomePaciente() + " recebeu uma dose de " + registroV.getNomeVacina() + " no " +
                                "dia " + formato.format(registroV.getDataVacinacao()) + ". A próxima dose deverá ser aplicada a partir do dia " + formato.format(registroV.getDataProximaVacinacao());
                        return ResponseEntity.badRequest().body(menssagemErro);
                    }
                } else if (registroVacinaDiferente.getId() != null) {
                    menssagemErro= "A primeira dose aplicada no paciente " + registroVacinaDiferente.getNomePaciente() + " foi:" +
                            registroVacinaDiferente.getNomeVacina() + " do laboratório " + registroVacinaDiferente.getFabricante() + ". Todas as doses devem ser aplicadas com " +
                            "o mesmo medicamento!";
                    return ResponseEntity.badRequest().body(menssagemErro);
                }
            } else {
                menssagemErro = "Não foi possível registrar sua solicitação pois o paciente " +
                        registroAtual.getNomePaciente() + " concluiu TODAS doses de " + registroAtual.getNomeVacina() + " no dia " + formato.format(registroAtual.getDataVacinacao());
                return ResponseEntity.badRequest().body(menssagemErro);
            }
        }
        return ResponseEntity.created(null).body(registroVacinacao);
    }

    public ResponseEntity<?> atualizarPorId(String id, RegistroVacinacao novosDadosDoRegistroVacinacao) {
        Optional<RegistroVacinacao> registroVacinacao = findById(id);

        List<RegistroVacinacao> vacinacoesDoPaciente = findByIdPaciente(registroVacinacao.get().getIdPaciente());
String msg= "";
        RegistroVacinacao registroAtual = new RegistroVacinacao();
        Map<String, RegistroVacinacao> response = new HashMap();
        for (RegistroVacinacao vacinacao : vacinacoesDoPaciente) {
            if (registroAtual.getId() == null) {
                registroAtual = vacinacao;
            }

            if (registroAtual.getDataVacinacao().before(vacinacao.getDataVacinacao())) {
                registroAtual = vacinacao;
            }
        }

        if (registroVacinacao.isPresent() && registroAtual.getId().equals(id)) {
            RegistroVacinacao novoPaciente = registroVacinacao.get();
            novoPaciente.setDataVacinacao(novosDadosDoRegistroVacinacao.getDataVacinacao());
            novoPaciente.setProfissionalSaude(novosDadosDoRegistroVacinacao.getProfissionalSaude());
            novoPaciente.setEstado(novosDadosDoRegistroVacinacao.getEstado());

            registroVacinacaoRepository.save(novoPaciente);
            return ResponseEntity.ok().body(novoPaciente);
        }
        msg = ("Só é possivel editar o ultimo registro de vacianação.");
        return ResponseEntity.badRequest().body(response);
    }

    public ResponseEntity<?> remove(String id) {
        Optional<RegistroVacinacao> registroVacina = findById(id);
        String msg = "";
        List<RegistroVacinacao> vacinacoesDoPaciente = findByIdPaciente(registroVacina.get().getIdPaciente());
        RegistroVacinacao registroAtual = new RegistroVacinacao();
        Map<String, RegistroVacinacao> response = new HashMap();
        RegistroVacinacao registroASerExcluido = new RegistroVacinacao();

        for (RegistroVacinacao vacinacao : vacinacoesDoPaciente) {
            if (registroAtual.getId() == null) {
                registroAtual = vacinacao;
            }
            if (vacinacao.getId().equals(id)) {
                registroASerExcluido = vacinacao;
            }

            if (registroAtual.getDataVacinacao().before(vacinacao.getDataVacinacao())) {
                registroAtual = vacinacao;
            }
        }

        if (registroVacina.isPresent() && registroAtual.getId().equals(id)) {
            registroVacina.ifPresent(value -> registroVacinacaoRepository.delete(value));
            msg = "Registro Excluido com sucesso!";
            return ResponseEntity.ok().body(response);
        }
        msg = "Só é permitido apenas a exclusão do ultimo registro.";
        return ResponseEntity.badRequest().body(response);
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

    public List<RegistroVacinacao> filtrarPorEstadoEFabricante(String estado, String fabricante, List<RegistroVacinacao> todosRegistros) {
        List<RegistroVacinacao> todosRegistrosRetorno = new ArrayList<>();

        if (!fabricante.isEmpty() && !fabricante.isEmpty()) {
            for (RegistroVacinacao registro : todosRegistros) {
                if (registro.getEstado().toLowerCase().equals(estado.toLowerCase()) && registro.getFabricante().toLowerCase().equals(fabricante.toLowerCase())) {
                    todosRegistrosRetorno.add(registro);
                }
            }
        }
        return todosRegistrosRetorno;
    }

    public List<RegistroVacinacao> filtrarPorEstado(String estado, String fabricante, List<RegistroVacinacao> todosRegistros) {
        List<RegistroVacinacao> todosRegistrosRetorno = new ArrayList<>();
        if (!estado.isEmpty()) {
            for (RegistroVacinacao registro : todosRegistros) {
                if (registro.getEstado().toLowerCase().equals(estado.toLowerCase()) && fabricante.isEmpty()) {
                    todosRegistrosRetorno.add(registro);
                }
            }
        }
        return todosRegistrosRetorno;
    }

    public List<RegistroVacinacao> filtrarPorFabricante(String fabricante, String estado, List<RegistroVacinacao> todosRegistros) {
        List<RegistroVacinacao> todosRegistrosRetorno = new ArrayList<>();
        if (!fabricante.isEmpty() && estado.isEmpty()) {
            for (RegistroVacinacao registro : todosRegistros) {
                if (registro.getFabricante().toLowerCase().equals(fabricante.toLowerCase())) {
                    todosRegistrosRetorno.add(registro);
                }
            }
        }
        return todosRegistrosRetorno;
    }

    public ResponseEntity<?> totalVacinasAplicadas(String estado, String fabricante) {
        Map<String, Integer> response = new HashMap();
        List<RegistroVacinacao> todosRegistros = registroVacinacaoRepository.findAll();
        List<RegistroVacinacao> todosRegistrosRetorno = new ArrayList<>();
String msg = "";
        if (estado.isEmpty() && fabricante.isEmpty()) {
            response.put("total_vacinas_aplicadas", (int) registroVacinacaoRepository.count());
            return ResponseEntity.ok(response);
        }

        todosRegistrosRetorno = filtrarPorEstadoEFabricante(estado, fabricante, todosRegistros);
        if (!todosRegistrosRetorno.isEmpty()) {
            response.put("total_vacinas_aplicadas", (int) todosRegistrosRetorno.size());
            return ResponseEntity.ok(response);
        }

        todosRegistrosRetorno = filtrarPorEstado(estado, fabricante, todosRegistros);
        if (!todosRegistrosRetorno.isEmpty()) {
            response.put("total_vacinas_aplicadas", (int) todosRegistrosRetorno.size());
            return ResponseEntity.ok(response);
        }

        todosRegistrosRetorno = filtrarPorFabricante(fabricante, estado, todosRegistros);
        if (!todosRegistrosRetorno.isEmpty()) {
            response.put("total_vacinas_aplicadas", (int) todosRegistrosRetorno.size());
            return ResponseEntity.ok(response);
        }
        if (estado.isEmpty() && estado.isEmpty()) {
            msg = ("Nenhum registro de vacinação encontrado");
            return ResponseEntity.ok(response);
        }

        msg = ("Nenhum registro de vacinação encontrado para o filtro: " + (!estado.isEmpty() ? "estado: " + estado : "") + " " + (!fabricante.isEmpty() ? "fabricante: " + fabricante : ""));
        return ResponseEntity.ok(msg);

    }

    public ResponseEntity<?> pacientesComDosesAtrasadas(String estado) {
        Date hoje = new Date();
        Map<String, Integer> response = new HashMap();
        List<RegistroVacinacao> todosRegistros = registroVacinacaoRepository.findAll();
        List<RegistroVacinacao> pacientesRetorno = new ArrayList<>();
        if (estado.isEmpty()){
            for (RegistroVacinacao registro : todosRegistros) {
                if (registro.getDataProximaVacinacao() != null && registro.getDataProximaVacinacao().before(hoje)) {
                    pacientesRetorno.add(registro);
                }
            }
        }else{
            for (RegistroVacinacao registro : todosRegistros) {
                if (registro.getDataProximaVacinacao() != null && registro.getDataProximaVacinacao().before(hoje) && registro.getEstado().equals(estado)) {
                    pacientesRetorno.add(registro);
                }
            }
        }

        if (pacientesRetorno.isEmpty()){
            if (estado.isEmpty()){
                response.put("paciente_vacina_atrasado", (Integer) 0);
                return ResponseEntity.ok(response);
            }else{
                response.put("paciente_vacina_atrasado", (Integer) 0);
                return ResponseEntity.ok(response);
            }

        }
        if (!estado.isEmpty()){
            response.put("paciente_vacina_atrasado", (Integer) pacientesRetorno.size());
            return ResponseEntity.ok(response);
        }

        response.put("paciente_vacina_atrasado", (Integer) pacientesRetorno.size());
        return ResponseEntity.ok(response);
    }

}