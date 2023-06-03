package com.example.bancamovil.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TransferenciasExterna implements Serializable {
    private int IdTransferencia;
    private Integer IdCuenta;
    private Integer IdBanco;
    private Date FechaTransferencia;
    private double Monto;
    private String Descripcion;
    private BancosExterno IdBancoNavigation;
    private CuentasBancaria IdCuentaNavigation;

    public int getIdTransferencia() {
        return IdTransferencia;
    }

    public void setIdTransferencia(int idTransferencia) {
        IdTransferencia = idTransferencia;
    }

    public Integer getIdCuenta() {
        return IdCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        IdCuenta = idCuenta;
    }

    public Integer getIdBanco() {
        return IdBanco;
    }

    public void setIdBanco(Integer idBanco) {
        IdBanco = idBanco;
    }

    public Date getFechaTransferencia() {
        return FechaTransferencia;
    }

    public void setFechaTransferencia(Date fechaTransferencia) {
        FechaTransferencia = fechaTransferencia;
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

    public BancosExterno getIdBancoNavigation() {
        return IdBancoNavigation;
    }

    public void setIdBancoNavigation(BancosExterno idBancoNavigation) {
        IdBancoNavigation = idBancoNavigation;
    }

    public CuentasBancaria getIdCuentaNavigation() {
        return IdCuentaNavigation;
    }

    public void setIdCuentaNavigation(CuentasBancaria idCuentaNavigation) {
        IdCuentaNavigation = idCuentaNavigation;
    }
}
