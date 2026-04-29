package com.fernando.auth.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fernando.auth.dto.UsuarioRequest;
import com.fernando.auth.dto.UsuarioResponse;
import com.fernando.auth.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Set<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable String username,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(request, username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UsuarioResponse> eliminar(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.eliminar(username));
    }
}
