package amycorp.parkea.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import amycorp.parkea.MapaActivity;
import amycorp.parkea.R;
import amycorp.parkea.fragments.ParqueaderosFacultadFragment;
import amycorp.parkea.fragments.RegistrarFragment;
import amycorp.parkea.models.Parqueadero;

public class ParqueaderoAdaptador extends RecyclerView.Adapter<ParqueaderoAdaptador.ViewHolder>

{

    private List<Parqueadero> list_parqueaderos;
    Context thiscontext;

    public ParqueaderoAdaptador(List<Parqueadero> list_parqueaderos) {
        this.list_parqueaderos = list_parqueaderos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nombre_parqueadero;
        private TextView disponibles;
        private TextView capacidad;
        private ImageView img_mapa;


        public ViewHolder(final View itemView)
        {
            super(itemView);
            nombre_parqueadero = (TextView) itemView.findViewById(R.id.nombre_parqueadero);
            disponibles = (TextView) itemView.findViewById(R.id.disponibles);
            capacidad = (TextView) itemView.findViewById(R.id.capacidad);
            img_mapa = (ImageView) itemView.findViewById(R.id.img_mapa);
        }


    }

    @Override
    public ParqueaderoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_parqueadero, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParqueaderoAdaptador.ViewHolder holder, final int position) {

        holder.disponibles.setText(String.valueOf(list_parqueaderos.get(position).getDisponibles()));
        holder.capacidad.setText(String.valueOf(list_parqueaderos.get(position).getCapacidad()));
        holder.nombre_parqueadero.setText(list_parqueaderos.get(position).getNombre());
        holder.nombre_parqueadero.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            thiscontext = v.getContext();
            Fragment fragment = new RegistrarFragment();
            Bundle args = new Bundle();
            args.putInt("facultad_id", list_parqueaderos.get(position).getFacultad_id());
            args.putInt("parqueadero_id", list_parqueaderos.get(position).getId());
            fragment.setArguments(args);
            ((AppCompatActivity)thiscontext).getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).addToBackStack(null).commit();
            }
        });

        holder.img_mapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapaActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_parqueaderos.size();
    }
}