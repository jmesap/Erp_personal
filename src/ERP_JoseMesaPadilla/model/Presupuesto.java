package ERP_JoseMesaPadilla.model;

import java.time.LocalDate;

public class Presupuesto {
    private int presupuestoId;
    private int proyectoId;
    private int clienteId;
    private double totalPresupuesto;
    private LocalDate fechaCreacion;
    private String estado;

    public Presupuesto(int presupuestoId, int proyectoId, int clienteId, double totalPresupuesto, LocalDate fechaCreacion, String estado) {
        this.presupuestoId = presupuestoId;
        this.proyectoId = proyectoId;
        this.clienteId = clienteId;
        this.totalPresupuesto = totalPresupuesto;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    // Getters y setters
    public int getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(int presupuestoId) {
        this.presupuestoId = presupuestoId;
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

    public double getTotalPresupuesto() {
        return totalPresupuesto;
    }

    public void setTotalPresupuesto(double totalPresupuesto) {
        this.totalPresupuesto = totalPresupuesto;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String generarNumeroPresupuesto() {
        return "P" + presupuestoId + "-" + LocalDate.now().getYear();
    }
}
