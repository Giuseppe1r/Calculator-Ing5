package com.example.dto;

import java.time.LocalDateTime;

public class OperacionResponseDto {
    private Long id;
    private String tipoOperacion;
    private double a;
    private double b;
    private double resultado;
    private LocalDateTime fechaCreacion;

    public OperacionResponseDto() {}

    public OperacionResponseDto(Long id, String tipoOperacion, double a, double b, double resultado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.tipoOperacion = tipoOperacion;
        this.a = a;
        this.b = b;
        this.resultado = resultado;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    public double getA() { return a; }
    public void setA(double a) { this.a = a; }

    public double getB() { return b; }
    public void setB(double b) { this.b = b; }

    public double getResultado() { return resultado; }
    public void setResultado(double resultado) { this.resultado = resultado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
