package com.example.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.OperacionRequestDto;
import com.example.dto.OperacionResponseDto;
import com.example.service.CalculadoraService;

import java.util.List;

/**
 * La politica de CORS se define de forma centralizada en {@link com.example.config.CorsConfig}
 * en lugar de con @CrossOrigin("*") para poder restringir los origenes por entorno.
 */
@RestController
@RequestMapping
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    public CalculadoraController(CalculadoraService calculadoraService) {
        this.calculadoraService = calculadoraService;
    }

    @PostMapping("/sumar")
    public ResponseEntity<OperacionResponseDto> sumar(@Valid @RequestBody OperacionRequestDto request) {
        return ResponseEntity.ok(calculadoraService.sumar(request));
    }

    @PostMapping("/restar")
    public ResponseEntity<OperacionResponseDto> restar(@Valid @RequestBody OperacionRequestDto request) {
        return ResponseEntity.ok(calculadoraService.restar(request));
    }

    @PostMapping("/multiplicar")
    public ResponseEntity<OperacionResponseDto> multiplicar(@Valid @RequestBody OperacionRequestDto request) {
        return ResponseEntity.ok(calculadoraService.multiplicar(request));
    }

    /**
     * La division por cero lanza DivisionPorCeroException y GlobalExceptionHandler
     * la convierte en un 400, por eso aqui el tipo de retorno ya es concreto.
     */
    @PostMapping("/dividir")
    public ResponseEntity<OperacionResponseDto> dividir(@Valid @RequestBody OperacionRequestDto request) {
        return ResponseEntity.ok(calculadoraService.dividir(request));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<OperacionResponseDto>> obtenerHistorial() {
        return ResponseEntity.ok(calculadoraService.obtenerHistorial());
    }
}
