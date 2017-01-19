package amycorp.parkea.models;


public class ParqueoPersona {

    Integer id;
    Integer persona_id;
    Integer facultad_id;
    Integer parqueadero_id;
    String placa;
    String fecha_ingreso;
    String hora_ingreso;
    String fecha_salida;
    String hora_salida;
    Double longitud;
    Double latitud;
    String estado;

    public ParqueoPersona(Integer id, Integer persona_id, Integer facultad_id, Integer parqueadero_id, String placa, String fecha_ingreso, String hora_ingreso, String fecha_salida, String hora_salida, Double longitud, Double latitud, String estado) {
        this.id = id;
        this.persona_id = persona_id;
        this.facultad_id = facultad_id;
        this.parqueadero_id = parqueadero_id;
        this.placa = placa;
        this.fecha_ingreso = fecha_ingreso;
        this.hora_ingreso = hora_ingreso;
        this.fecha_salida = fecha_salida;
        this.hora_salida = hora_salida;
        this.longitud = longitud;
        this.latitud = latitud;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(Integer persona_id) {
        this.persona_id = persona_id;
    }

    public Integer getFacultad_id() {
        return facultad_id;
    }

    public void setFacultad_id(Integer facultad_id) {
        this.facultad_id = facultad_id;
    }

    public Integer getParqueadero_id() {
        return parqueadero_id;
    }

    public void setParqueadero_id(Integer parqueadero_id) {
        this.parqueadero_id = parqueadero_id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setEstado(String estado) { this.estado = estado; }

    public String getEstado() { return estado; }


}
