package amycorp.parkea.models;

/**
 * Created by astrophoenix on 26/12/16.
 */

public class Placa {

    String nombre;

    public Placa(String nombre) {
        nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
