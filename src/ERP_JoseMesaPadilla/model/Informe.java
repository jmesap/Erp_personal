/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP_JoseMesaPadilla.model;

import java.time.LocalDate;
import java.util.Date;

public class Informe {
    private int informeId;
    private int proyectoId;
    private Date fechaGeneracion;
    private String tipoInforme; // "Avance", "Final", "Otro"
    private String contenido;


    // Constructor completo
    public Informe(int informeId, int proyectoId, Date fechaGeneracion, String tipoInforme, String contenido) {
        this.informeId = informeId;
        this.proyectoId = proyectoId;
        this.fechaGeneracion = fechaGeneracion;
        this.tipoInforme = tipoInforme;
        this.contenido = contenido;
    }

    // Getters y Setters
    public int getInformeId() {
        return informeId;
    }

    public void setInformeId(int informeId) {
        this.informeId = informeId;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}

