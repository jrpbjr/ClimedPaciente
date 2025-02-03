package com.climedx.inosystem.service;

import com.climedx.inosystem.dto.CaracteristicasDTO;
import com.climedx.inosystem.dto.EnderecoDTO;
import com.climedx.inosystem.dto.PacienteDTO;
import com.climedx.inosystem.dto.TelefoneDTO;
import com.climedx.inosystem.enums.EstadoCivil;
import com.climedx.inosystem.enums.Genero;
import com.climedx.inosystem.enums.TipoTelefone;
import com.climedx.inosystem.model.Paciente;
import com.climedx.inosystem.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<String> listarTiposTelefone() {
        return Arrays.stream(TipoTelefone.values())
                .map(TipoTelefone::getDescricao)
                .collect(Collectors.toList());
    }

    // Método para listar estados civis
    public List<String> listarEstadosCivis() {
        return Arrays.stream(EstadoCivil.values())
                .map(EstadoCivil::getDescricao)
                .collect(Collectors.toList());
    }

    // Método para listar os gêneros
    public List<String> listarGeneros() {
        return Arrays.stream(Genero.values())
                .map(Genero::getDescricao)
                .collect(Collectors.toList());
    }

    public Paciente salvar(Paciente paciente) {
        if (paciente.getCaracteristicas() != null) {
            paciente.calcularIdade(); // Calcula a idade antes de salvar
            paciente.getCaracteristicas().setPaciente(paciente);
        }

        if (paciente.getTelefones() != null) {
            paciente.getTelefones().forEach(tel -> tel.setPaciente(paciente));
        }

        if (paciente.getEnderecos() != null) {
            paciente.getEnderecos().forEach(end -> end.setPaciente(paciente));
        }

        return pacienteRepository.save(paciente);
    }

//Listar todos os pacientes e atualizar idade no banco
@Transactional
public List<PacienteDTO> listarTodos() {
    List<Paciente> pacientes = pacienteRepository.findAll();

    pacientes.forEach(paciente -> {
        paciente.calcularIdade();
        pacienteRepository.save(paciente);
    });

    return pacientes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
}
// Buscar um paciente por ID e atualizar idade no banco
@Transactional
public Optional<Paciente> buscarPorId(Long id) {
    Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

    if (pacienteOptional.isPresent()) {
        Paciente paciente = pacienteOptional.get();
        paciente.calcularIdade();
        pacienteRepository.save(paciente);
    }

    return pacienteOptional;
}


    // Atualizar um paciente existente
    public Optional<Paciente> atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setPacNome(pacienteAtualizado.getPacNome());
            paciente.setPacCpf(pacienteAtualizado.getPacCpf());
            paciente.setPacEmail(pacienteAtualizado.getPacEmail());
            paciente.setPacEstcivil(pacienteAtualizado.getPacEstcivil());
            paciente.setTelefones(pacienteAtualizado.getTelefones());
            paciente.setEnderecos(pacienteAtualizado.getEnderecos());
            paciente.calcularIdade(); // Atualiza idade
            paciente.setCaracteristicas(pacienteAtualizado.getCaracteristicas());
            return pacienteRepository.save(paciente);
        });
    }

    // Remover paciente por ID
    public boolean remover(Long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //  Tornando o método PUBLIC para que o Controller possa acessá-lo
    public PacienteDTO converterParaDTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setPacId(paciente.getPacId());
        dto.setPacNome(paciente.getPacNome());
        dto.setPacCpf(paciente.getPacCpf());
        dto.setPacNasc(paciente.getPacNasc());  // Adiciona a data de nascimento
        dto.setPacIdade(paciente.getPacIdade()); // Adiciona a idade calculada
        dto.setPacEmail(paciente.getPacEmail());

        dto.setPacEstcivil(paciente.getPacEstcivil());

        dto.setTelefones(paciente.getTelefones().stream()
                .map(t -> new TelefoneDTO(t.getTipo(), t.getNumero()))
                .collect(Collectors.toList()));

        dto.setEnderecos(paciente.getEnderecos().stream()
                .map(e -> new EnderecoDTO(e.getRua(), e.getBairro(), e.getCidade(), e.getEstado(), e.getCep()))
                .collect(Collectors.toList()));

        if (paciente.getCaracteristicas() != null) {
            dto.setCaracteristicas(new CaracteristicasDTO(
                    paciente.getCaracteristicas().getPeso(),
                    paciente.getCaracteristicas().getAltura(),
                    paciente.getCaracteristicas().getGenero()));
        }

        return dto;
    }
}