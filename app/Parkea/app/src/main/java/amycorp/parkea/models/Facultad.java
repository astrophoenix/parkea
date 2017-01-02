package amycorp.parkea.models;


public class Facultad {

    private Integer id;
    private String nombre;
    private Integer num_parqueos;

    public Facultad(Integer id, String nombre, Integer num_parqueos) {
        this.id = id;
        this.nombre = nombre;
        this.num_parqueos = num_parqueos;
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

    public Integer getNum_parqueos() {
        return num_parqueos;
    }

    public void setNum_parqueos(Integer num_parqueos) {
        this.num_parqueos = num_parqueos;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
