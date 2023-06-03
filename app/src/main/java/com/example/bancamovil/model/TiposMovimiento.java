package com.example.bancamovil.model;
import java.io.Serializable;
import java.util.List;

public class TiposMovimiento implements Serializable {
    private int IdTipoMovimiento;
    private String Nombre;
    private List<Movimiento> Movimientos;

    public int getIdTipoMovimiento() {
        return IdTipoMovimiento;
    }

    public void setIdTipoMovimiento(int idTipoMovimiento) {
        IdTipoMovimiento = idTipoMovimiento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public List<Movimiento> getMovimientos() {
        return Movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        Movimientos = movimientos;
    }
}
