package com.example.service;

import java.util.List;

import com.example.dto.OperacionRequestDto;
import com.example.dto.OperacionResponseDto;

public interface CalculadoraService {
    OperacionResponseDto sumar(OperacionRequestDto request);
    OperacionResponseDto restar(OperacionRequestDto request);
    OperacionResponseDto multiplicar(OperacionRequestDto request);
    OperacionResponseDto dividir(OperacionRequestDto request);
    List<OperacionResponseDto> obtenerHistorial();
}
