/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcmclient.classes;

/**
 *
 * @author Nacho
 */
public class Usuario {
    
    private Long id;
    
    private String nombre;
    
    private String aliasCuenta;
        
    private String cola;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String aliasCuenta, String cola) {
        this.id = id;
        this.nombre = nombre;
        this.aliasCuenta = aliasCuenta;
        this.cola = cola;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAliasCuenta() {
        return aliasCuenta;
    }

    public void setAliasCuenta(String aliasCuenta) {
        this.aliasCuenta = aliasCuenta;
    }

    public String getCola() {
        return cola;
    }

    public void setCola(String cola) {
        this.cola = cola;
    }
    
    
}
