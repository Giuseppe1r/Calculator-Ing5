package com.example.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.OperacionRequestDto;
import com.example.dto.OperacionResponseDto;
import com.example.entity.Operacion;
import com.example.exception.DivisionPorCeroException;
import com.example.mapper.OperacionMapper;
import com.example.repository.OperacionRepository;
import com.example.service.CalculadoraService;

import java.util.List;

@Service
public class CalculadoraServiceImpl implements CalculadoraService {

    private static final String SUMA = "Suma";
    private static final String RESTA = "Resta";
    private static final String MULTIPLICACION = "Multiplicacion";
    private static final String DIVISION = "Division";

    private final OperacionRepository operacionRepository;
    private final OperacionMapper operacionMapper;

    public CalculadoraServiceImpl(OperacionRepository operacionRepository, OperacionMapper operacionMapper) {
        this.operacionRepository = operacionRepository;
        this.operacionMapper = operacionMapper;
    }

    @Override
    @Transactional
    public OperacionResponseDto sumar(OperacionRequestDto request) {
        return registrar(SUMA, request, request.getA() + request.getB());
    }

    @Override
    @Transactional
    public OperacionResponseDto restar(OperacionRequestDto request) {
        return registrar(RESTA, request, request.getA() - request.getB());
    }

    @Override
    @Transactional
    public OperacionResponseDto multiplicar(OperacionRequestDto request) {
        return registrar(MULTIPLICACION, request, request.getA() * request.getB());
    }

    @Override
    @Transactional
    public OperacionResponseDto dividir(OperacionRequestDto request) {
        if (request.getB() == 0) {
            throw new DivisionPorCeroException();
        }
        return registrar(DIVISION, request, request.getA() / request.getB());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperacionResponseDto> obtenerHistorial() {
        return operacionMapper.toDtoList(operacionRepository.findTop5ByOrderByIdDesc());
    }

    private OperacionResponseDto registrar(String tipo, OperacionRequestDto request, double resultado) {
        Operacion guardada = operacionRepository.save(
                new Operacion(tipo, request.getA(), request.getB(), resultado));
        return operacionMapper.toDto(guardada);
    }
}
