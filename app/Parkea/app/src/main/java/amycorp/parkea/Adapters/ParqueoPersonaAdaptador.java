package amycorp.parkea.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.LoginActivity;
import amycorp.parkea.MapaActivity;
import amycorp.parkea.PrincipalActivity;
import amycorp.parkea.R;
import amycorp.parkea.fragments.ParqueaderosFacultadFragment;
import amycorp.parkea.fragments.RegistrarFragment;
import amycorp.parkea.models.Global;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.models.ParqueoPersona;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParqueoPersonaAdaptador extends RecyclerView.Adapter<ParqueoPersonaAdaptador.ViewHolder>

{

    private List<ParqueoPersona> list_parqueo_persona;
    Context thiscontext;

    public ParqueoPersonaAdaptador(List<ParqueoPersona> list_parqueo_persona) {
        this.list_parqueo_persona = list_parqueo_persona;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView fecha_hora_ingreso;
        private TextView fecha_hora_salida;
        private ImageView estado_parqueo;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            fecha_hora_ingreso = (TextView) itemView.findViewById(R.id.list_fecha_hora_ingreso);
            fecha_hora_salida = (TextView) itemView.findViewById(R.id.list_fecha_hora_salida);
            estado_parqueo = (ImageView) itemView.findViewById(R.id.list_estado_parqueo);

        }

    }

    @Override
    public ParqueoPersonaAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_parqueo_persona, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ParqueoPersonaAdaptador.ViewHolder holder, final int position) {


        String fecha_ingreso = list_parqueo_persona.get(position).getFecha_ingreso();
        String fecha_salida = list_parqueo_persona.get(position).getFecha_salida();
        String hora_ingreso = list_parqueo_persona.get(position).getHora_ingreso();
        String hora_salida = list_parqueo_persona.get(position).getHora_salida();

        if (hora_salida == null)
            hora_salida = "";

        if (hora_ingreso == null)
            hora_ingreso = "";

        if (fecha_ingreso == null)
            fecha_ingreso = "";

        if (fecha_salida == null)
            fecha_salida = "";

        String msj_ingreso = "";
        String msj_salida = "";
        if (!fecha_ingreso.equals("") && !hora_ingreso.equals(""))
            msj_ingreso = "Entrada: "+ fecha_ingreso + " | " + hora_ingreso;
        else
            msj_salida = "Entrada: No";

        if (!fecha_salida.equals("") && !hora_salida.equals(""))
            msj_salida = "Salida: " + fecha_salida + " | " + hora_salida;
        else
            msj_salida = "Salida: No";


        holder.fecha_hora_ingreso.setText(String.valueOf(msj_ingreso));
        holder.fecha_hora_salida.setText(String.valueOf(msj_salida));

        String estado = list_parqueo_persona.get(position).getEstado();

        if (estado.equals("A"))
            holder.estado_parqueo.setImageResource(R.drawable.unlocked);
        else
            holder.estado_parqueo.setImageResource(R.drawable.padlock);

        /*holder.estado_parqueo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Global.en_area){
                    thiscontext = v.getContext();
                    Integer parqueo_id = list_parqueo_persona.get(position).getId();
                    actualizarParqueoPersona(parqueo_id, holder);
                }else{
                    Toast.makeText(thiscontext, String.valueOf("No puede actualizar registro dentro del parqueadero."), Toast.LENGTH_LONG).show();
                }

            }
        });*/

    }

    private void actualizarParqueoPersona(Integer parqueo_id, final ParqueoPersonaAdaptador.ViewHolder holder)
    {
        APIService mApiService = Controller.getInterfaceService();
        Call<ParqueoPersona> mService = mApiService.actualizarEstadoParqueoPersonaAPI(parqueo_id);
        mService.enqueue(new Callback<ParqueoPersona>() {
            @Override
            public void onResponse(Call<ParqueoPersona> call, Response<ParqueoPersona> response) {

                //Respuesta Exitosa del Servidor
                if(response.isSuccessful())
                {
                    ParqueoPersona resp = response.body();
                    String estado = resp.getEstado();
                    String fecha_ingreso = resp.getFecha_ingreso();
                    String hora_ingreso = resp.getHora_ingreso();
                    String fecha_salida = resp.getFecha_salida();
                    String hora_salida = resp.getHora_salida();
                    if (fecha_ingreso == null)
                        fecha_ingreso = "";
                    if (hora_ingreso == null)
                        hora_ingreso = "";
                    if (fecha_salida == null)
                        fecha_salida = "";
                    if (hora_salida == null)
                        hora_salida = "";

                    String msj_ingreso = "";
                    String msj_salida = "";
                    if (!fecha_ingreso.equals("") && !hora_ingreso.equals(""))
                        msj_ingreso = "Entrada: "+ fecha_ingreso + " | " + hora_ingreso;
                    else
                        msj_salida = "Entrada: No";

                    if (!fecha_salida.equals("") && !hora_salida.equals(""))
                        msj_salida = "Salida: " + fecha_salida + " | " + hora_salida;
                    else
                        msj_salida = "Salida: No";

                    if(estado.trim().equals("A")){
                        holder.estado_parqueo.setImageResource(R.drawable.unlocked);
                        holder.fecha_hora_ingreso.setText(String.valueOf(msj_ingreso));
                        holder.fecha_hora_salida.setText(String.valueOf(msj_salida));
                    }else if(estado.trim().equals("I")) {
                        holder.estado_parqueo.setImageResource(R.drawable.padlock);
                        holder.fecha_hora_ingreso.setText(String.valueOf(msj_ingreso));
                        holder.fecha_hora_salida.setText(String.valueOf(msj_salida));
                    }else {
                        Toast.makeText(thiscontext, "Actualización No realizada", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ParqueoPersona> call, Throwable t) {
                call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2",t.getCause()+"");
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list_parqueo_persona.size();
    }
}