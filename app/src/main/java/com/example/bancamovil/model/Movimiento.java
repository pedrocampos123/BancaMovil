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
    private String CuentaOrigen;
    private String CuentaDestino;
    private CuentasBancaria IdCuentaNavigation;
    private TiposMovimiento IdTipoMovimientoNavigation;

    public Movimiento(int idMovimiento, Integer idCuenta, Integer idTipoMovimiento, Date fechaMovimiento, double monto, String descripcion, String cuentaOrigen, String cuentaDestino, CuentasBancaria idCuentaNavigation, TiposMovimiento idTipoMovimientoNavigation) {
        IdMovimiento = idMovimiento;
        IdCuenta = idCuenta;
        IdTipoMovimiento = idTipoMovimiento;
        FechaMovimiento = fechaMovimiento;
        Monto = monto;
        Descripcion = descripcion;
        CuentaOrigen = cuentaOrigen;
        CuentaDestino = cuentaDestino;
        IdCuentaNavigation = idCuentaNavigation;
        IdTipoMovimientoNavigation = idTipoMovimientoNavigation;
    }

    public Movimiento() {

    }

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

    public String getCuentaOrigen() {
        return CuentaOrigen;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        CuentaOrigen = cuentaOrigen;
    }

    public String getCuentaDestino() {
        return CuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) {
        CuentaDestino = cuentaDestino;
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
