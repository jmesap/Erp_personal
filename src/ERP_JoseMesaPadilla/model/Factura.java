/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP_JoseMesaPadilla.model;

import java.util.Date;


public class Factura {
    private int facturaId;
    private int proyectoId;
    private int clienteId;
    private double totalFactura;
    private Date fechaEmision;
    private String estado;

    // Constructor
    public Factura(int facturaId, int proyectoId, int clienteId, double totalFactura, Date fechaEmision, String estado) {
        this.facturaId = facturaId;
        this.proyectoId = proyectoId;
        this.clienteId = clienteId;
        this.totalFactura = totalFactura;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
    }

    // Getters y Setters
    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para generar el número de factura con formato F<ID>-AÑO
    public String generarNumeroFactura() {
        return "F" + facturaId + "-" + (fechaEmision != null ? fechaEmision.toString().substring(0, 4) : "2024");
    }
}
