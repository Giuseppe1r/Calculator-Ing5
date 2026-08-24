package com.example.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Los operandos son Double (y no double) a proposito: con el primitivo, un
 * campo ausente en el JSON se convertia silenciosamente en 0 y la operacion se
 * ejecutaba con datos incompletos. Con el wrapper + @NotNull la peticion
 * invalida se rechaza con un 400 explicito.
 */
public class OperacionRequestDto {

    @NotNull(message = "es obligatorio")
    private Double a;

    @NotNull(message = "es obligatorio")
    private Double b;

    public OperacionRequestDto() {}

    public OperacionRequestDto(Double a, Double b) {
        this.a = a;
        this.b = b;
    }

    public Double getA() { return a; }
    public void setA(Double a) { this.a = a; }

    public Double getB() { return b; }
    public void setB(Double b) { this.b = b; }
}
