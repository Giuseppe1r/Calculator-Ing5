package com.example.exception;

public class DivisionPorCeroException extends RuntimeException {

    public DivisionPorCeroException() {
        super("No se puede dividir entre cero.");
    }
}
