package com.arojas.test.springboot.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bancos")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name = "total_tranferencias")
    private int totalTransfrencia;

    public Banco() {
    }

    public Banco(Long id, String nombre, int totalTransfrencia) {
        this.id = id;
        this.nombre = nombre;
        this.totalTransfrencia = totalTransfrencia;
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

    public int getTotalTransfrencia() {
        return totalTransfrencia;
    }

    public void setTotalTransfrencia(int totalTransfrencia) {
        this.totalTransfrencia = totalTransfrencia;
    }
}
