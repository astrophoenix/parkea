package amycorp.parkea.services;
import java.util.List;

import amycorp.parkea.models.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
/*
* Archivo de Conexiones y Acciones  al Servidor por medio de Urls
* */
public interface APIService {

    //Url de conexión base que apunta a la raíz del servidor para que el resto de conexiones
    // puedan ejecutarse.

    //final String BASE_URL = "http://10.0.2.2:8000/backend/"; //local emulador a bd
    //final String BASE_URL = "http://192.168.100.6:8000/backend/"; //local dispositivo a bd
    final String BASE_URL = "http://198.199.122.165:8000/backend/"; // remoto dispositivo a bd


    @GET("validar_login/{correo}/{clave}")
    Call<RespuestaAPIServidor> validarUsuarioAPI(@Path("correo") String email, @Path("clave") String password);

    @GET("registrar_usuario/{nombre}/{apellido}/{placa}/{correo}/{clave}/")
    Call<RespuestaAPIServidor> registrarUsuarioAPI(@Path("nombre") String nombre,
                                                   @Path("apellido") String apellido,
                                                   @Path("placa") String placa,
                                                   @Path("correo") String correo,
                                                   @Path("clave") String clave );


    @GET("obtener_usuario/{usuario_id}/")
    Call<Usuario> obtenerUsuarioAPI(@Path("usuario_id") Integer usuario_id);

    @GET("registrar_parqueo_persona/{parqueadero_id}/{placa}/{longitud}/{latitud}/{usuario_id}/")
    Call<RespuestaAPIServidor> registrarParqueoPersonaAPI(@Path("parqueadero_id") Integer parqueadero_id,
                                                          @Path("placa") String placa,
                                                          @Path("longitud") Double longitud,
                                                          @Path("latitud") Double latitud,
                                                          @Path("usuario_id") Integer usuario_id
                                                          );

    @GET("registrar_placa_usuario/{usuario_id}/{placa}")
    Call<RespuestaAPIServidor> registrarPlacaUsuarioAPI(@Path("usuario_id") Integer usuario_id, @Path("placa") String placa);

    @GET("obtener_facultades/")
    Call<List<Facultad>> obtenerFacultadesAPI();

    @GET("obtener_parqueaderosxfacultad/{facultad_id}/")
    Call<List<Parqueadero>> obtenerParqueaderosXFacultadAPI(@Path("facultad_id") Integer facultad_id);

    @GET("obtener_placasxpersona/{persona_id}/")
    Call<List<Placa>> obtenerPlacasXPersonaAPI(@Path("persona_id") Integer persona_id);

    @GET("obtener_historial_parqueo_persona/{usuario_id}/")
    Call<List<ParqueoPersona>> obtenerHistorialXPersonaAPI(@Path("usuario_id") Integer usuario_id);

    @GET("obtener_notificaciones_recompensa/{usuario_id}/")
    Call<RespuestaAPIServidor> obtenerNotificacionesRecompensaAPI(@Path("usuario_id") Integer usuario_id);


    @GET("actualizar_estado_parqueo_persona/{parqueo_id}/")
    Call<ParqueoPersona> actualizarEstadoParqueoPersonaAPI(@Path("parqueo_id") Integer parqueo_id);

    @GET("registrar_reporte_parqueos/{parqueadero_id}/{num_parqueos_ocupados}/{usuario_id}/")
    Call<RespuestaAPIServidor> registrarReporteParqueosAPI(@Path("parqueadero_id") Integer parqueo_id,
                                                     @Path("num_parqueos_ocupados") Integer num_parqueos_ocupados,
                                                     @Path("usuario_id") Integer usuario_id );

    @GET("obtener_eventos/")
    Call<List<Noticia>> obtenerEventosAPI();

    @GET("obtener_recompensaxusuario/{usuario_id}/")
    Call<List<Noticia>> obtenerRecompensaXUsuarioAPI(@Path("usuario_id") Integer usuario_id);

    @GET("obtener_rating_usuario/{usuario_id}/")
    Call<RespuestaAPIServidor> obtenerRatingUsuarioAPI(@Path("usuario_id") Integer usuario_id);

    @GET("verificar_usuario_area_parqueadero/{usuario_id}/{longitud}/{latitud}")
    Call<RespuestaAPIServidor> verificarUsuarioAreaParqueaderoAPI(@Path("usuario_id") Integer usuario_id,
                                                               @Path("longitud") Double longitud,
                                                               @Path("latitud") Double latitud );



    @GET("verificar_registro_reporte_parqueo_activo/{usuario_id}/")
    Call<RespuestaAPIServidor> verificarRegistroReporteParqueoActivoAPI(@Path("usuario_id") Integer usuario_id);
}


