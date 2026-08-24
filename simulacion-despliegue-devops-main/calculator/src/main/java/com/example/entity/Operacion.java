package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operaciones")
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_operacion", nullable = false)
    private String tipoOperacion;

    @Column(name = "operando_a", nullable = false)
    private double operandoA;

    @Column(name = "operando_b", nullable = false)
    private double operandoB;

    @Column(nullable = false)
    private double resultado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Operacion() {}

    public Operacion(String tipoOperacion, double operandoA, double operandoB, double resultado) {
        this.tipoOperacion = tipoOperacion;
        this.operandoA = operandoA;
        this.operandoB = operandoB;
        this.resultado = resultado;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTipoOperacion() { return tipoOperacion; }
    public double getOperandoA() { return operandoA; }
    public double getOperandoB() { return operandoB; }
    public double getResultado() { return resultado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}