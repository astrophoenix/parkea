package amycorp.parkea.models;


public class Parqueadero {

    private Integer id;
    private Integer facultad_id;
    private String nombre;
    private Integer capacidad;
    private Integer disponibles;

    public Parqueadero(Integer id, String nombre, Integer capacidad, Integer disponibles, Integer parqueadero_id) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.disponibles = disponibles;
        this.facultad_id = parqueadero_id;
    }

    public Integer getFacultad_id() {
        return facultad_id;
    }

    public void setFacultad_id(Integer parqueadero_id) {
        this.facultad_id = parqueadero_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(Integer disponibles) {
        this.disponibles = disponibles;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
