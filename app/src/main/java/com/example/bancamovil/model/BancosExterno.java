package com.example.bancamovil.model;
import java.io.Serializable;
import java.util.List;

public class BancosExterno implements Serializable {
    private int IdBanco;
    private String NombreBanco;
    private String NumeroCuenta;
    private List<TransferenciasExterna> TransferenciasExternas;

    public int getIdBanco() {
        return IdBanco;
    }

    public void setIdBanco(int idBanco) {
        IdBanco = idBanco;
    }

    public String getNombreBanco() {
        return NombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        NombreBanco = nombreBanco;
    }

    public String getNumeroCuenta() {
        return NumeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        NumeroCuenta = numeroCuenta;
    }

    public List<TransferenciasExterna> getTransferenciasExternas() {
        return TransferenciasExternas;
    }

    public void setTransferenciasExternas(List<TransferenciasExterna> transferenciasExternas) {
        TransferenciasExternas = transferenciasExternas;
    }
}