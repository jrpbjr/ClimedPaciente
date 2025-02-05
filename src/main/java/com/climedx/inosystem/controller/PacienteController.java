package com.climedx.inosystem.controller;

import com.climedx.inosystem.dto.PacienteDTO;
import com.climedx.inosystem.model.Paciente;
import com.climedx.inosystem.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/tipos-telefone")
    public ResponseEntity<List<String>> listarTiposTelefone() {
        return ResponseEntity.ok(pacienteService.listarTiposTelefone());
    }

    @GetMapping("/estados-civis")
    public ResponseEntity<List<String>> listarEstadosCivis() {
        return ResponseEntity.ok(pacienteService.listarEstadosCivis());
    }

    @GetMapping("/generos")
    public ResponseEntity<List<String>> listarGeneros() {
        return ResponseEntity.ok(pacienteService.listarGeneros());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> criarPaciente(@RequestBody Paciente paciente) {
        Paciente novoPaciente = pacienteService.salvar(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.converterParaDTO(novoPaciente));
    }

    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listarPacientes(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listarTodos(pageable));
    }

    // Buscar paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscarPacientePorId(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(id);
        return paciente.map(value -> ResponseEntity.ok(pacienteService.converterParaDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar paciente por ID (Correção aplicada)
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> atualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteAtualizado) {
        Optional<Paciente> pacienteSalvo = pacienteService.atualizarPaciente(id, pacienteAtualizado);

        return pacienteSalvo.map(p -> ResponseEntity.ok(pacienteService.converterParaDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Remover paciente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPaciente(@PathVariable Long id) {
        if (pacienteService.buscarPorId(id).isPresent()) {
            pacienteService.remover(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}