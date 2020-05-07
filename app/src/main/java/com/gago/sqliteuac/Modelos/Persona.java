package com.gago.sqliteuac.Modelos;

public class Persona {
    private int cedula;
    private String nombre;
    private int estrato;
    private int salario;
    private int nivel_educativo;

    public Persona(int cedula, String nombre, int estrato, int salario, int nivel_educativo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.estrato = estrato;
        this.salario = salario;
        this.nivel_educativo = nivel_educativo;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstrato() {
        return estrato;
    }

    public void setEstrato(int estrato) {
        this.estrato = estrato;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    public int getNivel_educativo() {
        return nivel_educativo;
    }

    public void setNivel_educativo(int nivel_educativo) {
        this.nivel_educativo = nivel_educativo;
    }
}
