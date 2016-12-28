package amycorp.parkea.services;
import java.util.List;

import amycorp.parkea.models.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    final String BASE_URL = "http://10.0.2.2:8000/backend/";
    //final String BASE_URL = "http://192.168.100.6:8000/backend/";

    @GET("validar_login/{correo}/{clave}")
    Call<RespuestaAPIServidor> validarUsuarioAPI(@Path("correo") String email, @Path("clave") String password);

    @GET("registrar_usuario/{nombre}/{apellido}/{correo}/{clave}")
    Call<RespuestaAPIServidor> registrarUsuarioAPI(@Path("nombre") String nombre,
                                                   @Path("apellido") String apellido,
                                                   @Path("correo") String email,
                                                   @Path("clave") String password );




    @GET("registrar_parqueo_persona/{parqueadero_id}/{placa}")
    Call<RespuestaAPIServidor> registrarParqueoPersonaAPI(@Path("parqueadero_id") Integer parqueadero_id, @Path("placa") String placa);


    @GET("obtener_facultades/")
    Call<List<Facultad>> obtenerFacultadesAPI();

    @GET("obtener_parqueaderosxfacultad/{facultad_id}/")
    Call<List<Parqueadero>> obtenerParqueaderosXFacultadAPI(@Path("facultad_id") Integer facultad_id);

    @GET("obtener_placasxpersona/{persona_id}/")
    Call<List<Placa>> obtenerPlacasXPersonaAPI(@Path("persona_id") Integer persona_id);












}


