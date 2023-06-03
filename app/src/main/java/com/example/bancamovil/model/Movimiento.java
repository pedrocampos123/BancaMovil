package com.example.bancamovil.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Movimiento implements Serializable {
    private int IdMovimiento;
    private Integer IdCuenta;
    private Integer IdTipoMovimiento;
    private Date FechaMovimiento;
    private double Monto;
    private String Descripcion;
    private CuentasBancaria IdCuentaNavigation;
    private TiposMovimiento IdTipoMovimientoNavigation;

    public int getIdMovimiento() {
        return IdMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        IdMovimiento = idMovimiento;
    }

    public Integer getIdCuenta() {
        return IdCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        IdCuenta = idCuenta;
    }

    public Integer getIdTipoMovimiento() {
        return IdTipoMovimiento;
    }

    public void setIdTipoMovimiento(Integer idTipoMovimiento) {
        IdTipoMovimiento = idTipoMovimiento;
    }

    public Date getFechaMovimiento() {
        return FechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        FechaMovimiento = fechaMovimiento;
    }

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double monto) {
        Monto = monto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public CuentasBancaria getIdCuentaNavigation() {
        return IdCuentaNavigation;
    }

    public void setIdCuentaNavigation(CuentasBancaria idCuentaNavigation) {
        IdCuentaNavigation = idCuentaNavigation;
    }

    public TiposMovimiento getIdTipoMovimientoNavigation() {
        return IdTipoMovimientoNavigation;
    }

    public void setIdTipoMovimientoNavigation(TiposMovimiento idTipoMovimientoNavigation) {
        IdTipoMovimientoNavigation = idTipoMovimientoNavigation;
    }
}
