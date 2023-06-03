package com.example.bancamovil.model;

import java.io.Serializable;
import java.util.List;

public class CuentasBancaria implements Serializable {
    private int IdCuenta;
    private Integer IdUsuario;
    private double Saldo;
    private String No_Cuenta;
    private String Descripcion;
    private Usuario IdUsuarioNavigation;
    private List<Movimiento> Movimientos;
    private List<TransferenciasExterna> TransferenciasExternas;

    public int getIdCuenta() {
        return IdCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        IdCuenta = idCuenta;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        IdUsuario = idUsuario;
    }

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        Saldo = saldo;
    }

    public String getNo_Cuenta() {
        return No_Cuenta;
    }

    public void setNo_Cuenta(String no_Cuenta) {
        No_Cuenta = no_Cuenta;
    }

    public String getDescripcion() { return Descripcion; }

    public void setDescripcion(String descripcion) { Descripcion = descripcion; }

    public Usuario getIdUsuarioNavigation() {
        return IdUsuarioNavigation;
    }

    public void setIdUsuarioNavigation(Usuario idUsuarioNavigation) {
        IdUsuarioNavigation = idUsuarioNavigation;
    }

    public List<Movimiento> getMovimientos() {
        return Movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        Movimientos = movimientos;
    }

    public List<TransferenciasExterna> getTransferenciasExternas() {
        return TransferenciasExternas;
    }

    public void setTransferenciasExternas(List<TransferenciasExterna> transferenciasExternas) {
        TransferenciasExternas = transferenciasExternas;
    }
}
