package com.climedx.inosystem.service;

import com.climedx.inosystem.dto.CaracteristicasDTO;
import com.climedx.inosystem.dto.EnderecoDTO;
import com.climedx.inosystem.dto.PacienteDTO;
import com.climedx.inosystem.dto.TelefoneDTO;
import com.climedx.inosystem.enums.EstadoCivil;
import com.climedx.inosystem.enums.Genero;
import com.climedx.inosystem.enums.TipoTelefone;
import com.climedx.inosystem.model.Caracteristicas;
import com.climedx.inosystem.model.Endereco;
import com.climedx.inosystem.model.Paciente;
import com.climedx.inosystem.model.Telefone;
import com.climedx.inosystem.repository.EnderecoRepository;
import com.climedx.inosystem.repository.PacienteRepository;
import com.climedx.inosystem.repository.TelefoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

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
            paciente.setPacInfantil(paciente.getPacIdade() <= 16); //Define pacInfantil
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
public Page<PacienteDTO> listarTodos(Pageable pageable) {
    Page<Paciente> pacientesPage = pacienteRepository.findAll(pageable);

    // Atualizar idade e infantil antes de converter
    pacientesPage.forEach(paciente -> {
        paciente.calcularIdade();
        paciente.setPacInfantil(paciente.getPacIdade() <= 16);
        pacienteRepository.save(paciente);
    });

    // Converter para DTO e retornar um Page<PacienteDTO>
    return pacientesPage.map(this::converterParaDTO);
}
// Buscar um paciente por ID e atualizar idade no banco
@Transactional
public Optional<Paciente> buscarPorId(Long id) {
    Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

    if (pacienteOptional.isPresent()) {
        Paciente paciente = pacienteOptional.get();
        paciente.calcularIdade();
        paciente.setPacInfantil(paciente.getPacIdade() <= 16); //Atualiza pacInfantil
        pacienteRepository.save(paciente);
    }

    return pacienteOptional;
}


    public Optional<Paciente> atualizarPaciente(Long id, Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id).map(paciente -> {

            paciente.setPacNome(pacienteAtualizado.getPacNome());
            paciente.setPacCpf(pacienteAtualizado.getPacCpf());
            paciente.setPacEmail(pacienteAtualizado.getPacEmail());
            paciente.setPacEstcivil(pacienteAtualizado.getPacEstcivil());
            paciente.setPacNasc(pacienteAtualizado.getPacNasc());

            // Atualizar Telefones corretamente
            List<Telefone> telefonesAtualizados = new ArrayList<>();
            for (Telefone telefone : pacienteAtualizado.getTelefones()) {
                Telefone telefoneExistente = telefoneRepository.findById(telefone.getTelId())
                        .orElse(telefone); // Se não existir, adicionamos como novo
                telefoneExistente.setNumero(telefone.getNumero());
                telefoneExistente.setTipo(telefone.getTipo());
                telefoneExistente.setPaciente(paciente); // Assegura que o telefone pertence ao paciente
                telefonesAtualizados.add(telefoneExistente);
            }
            paciente.getTelefones().clear();
            paciente.getTelefones().addAll(telefonesAtualizados);

            // Atualizar Endereços corretamente
            List<Endereco> enderecosAtualizados = new ArrayList<>();
            for (Endereco endereco : pacienteAtualizado.getEnderecos()) {
                Endereco enderecoExistente = enderecoRepository.findById(endereco.getEnd_id())
                        .orElse(endereco);
                enderecoExistente.setRua(endereco.getRua());
                enderecoExistente.setBairro(endereco.getBairro());
                enderecoExistente.setCidade(endereco.getCidade());
                enderecoExistente.setEstado(endereco.getEstado());
                enderecoExistente.setCep(endereco.getCep());
                enderecoExistente.setPaciente(paciente);
                enderecosAtualizados.add(enderecoExistente);
            }
            paciente.getEnderecos().clear();
            paciente.getEnderecos().addAll(enderecosAtualizados);

            // 3️⃣ Atualizar Características (se existirem)
            if (pacienteAtualizado.getCaracteristicas() != null) {
                Caracteristicas caracteristicasExistentes = paciente.getCaracteristicas();
                if (caracteristicasExistentes == null) {
                    paciente.setCaracteristicas(pacienteAtualizado.getCaracteristicas());
                } else {
                    caracteristicasExistentes.setPeso(pacienteAtualizado.getCaracteristicas().getPeso());
                    caracteristicasExistentes.setAltura(pacienteAtualizado.getCaracteristicas().getAltura());
                    caracteristicasExistentes.setGenero(pacienteAtualizado.getCaracteristicas().getGenero());
                }
            }

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
        dto.setPacInfantil(paciente.getPacIdade() <= 16); //Define pacInfantil no DTO
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