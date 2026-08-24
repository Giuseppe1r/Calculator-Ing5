package com.example.mapper;


import org.springframework.stereotype.Component;

import com.example.dto.OperacionResponseDto;
import com.example.entity.Operacion;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OperacionMapper {

    public OperacionResponseDto toDto(Operacion entity) {
        if (entity == null) {
            return null;
        }
        return new OperacionResponseDto(
                entity.getId(),
                entity.getTipoOperacion(),
                entity.getOperandoA(),
                entity.getOperandoB(),
                entity.getResultado(),
                entity.getFechaCreacion()
        );
    }

    public List<OperacionResponseDto> toDtoList(List<Operacion> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
