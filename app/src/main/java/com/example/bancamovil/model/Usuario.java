package com.example.bancamovil.model;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    private int IdUsuario;
    private String Nombre;
    private String Apellido;
    private String CorreoElectronico;
    private String Contrasena;
    private List<CuentasBancaria> CuentasBancaria;

    public Usuario(int idUsuario, String nombre, String apellido, String correoElectronico, String contrasena) {
        IdUsuario = idUsuario;
        Nombre = nombre;
        Apellido = apellido;
        CorreoElectronico = correoElectronico;
        Contrasena = contrasena;
    }

    public Usuario() {

    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreoElectronico() {
        return CorreoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        CorreoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public List<CuentasBancaria> getCuentasBancaria() {
        return CuentasBancaria;
    }

    public void setCuentasBancaria(List<CuentasBancaria> cuentasBancaria) {
        CuentasBancaria = cuentasBancaria;
    }
}
