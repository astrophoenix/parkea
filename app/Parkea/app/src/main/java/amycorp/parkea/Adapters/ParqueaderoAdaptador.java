package amycorp.parkea.Adapters;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import amycorp.parkea.ParqueaderosFacultadActivity;
import amycorp.parkea.R;
import amycorp.parkea.RegistrarParqueoActivity;
import amycorp.parkea.models.Facultad;
import amycorp.parkea.models.Parqueadero;

public class ParqueaderoAdaptador extends RecyclerView.Adapter<ParqueaderoAdaptador.ViewHolder>

{

    private List<Parqueadero> list_parqueaderos;

    public ParqueaderoAdaptador(List<Parqueadero> list_parqueaderos) {
        this.list_parqueaderos = list_parqueaderos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nombre_parqueadero;
        private TextView disponibles;
        private TextView capacidad;


        public ViewHolder(final View itemView)
        {
            super(itemView);
            nombre_parqueadero = (TextView) itemView.findViewById(R.id.nombre_parqueadero);
            disponibles = (TextView) itemView.findViewById(R.id.disponibles);
            capacidad = (TextView) itemView.findViewById(R.id.capacidad);
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

        //holder.disponibles.setText(list_parqueaderos.get(position).getDisponibles());
        //holder.capacidad.setText(list_parqueaderos.get(position).getCapacidad());
        holder.nombre_parqueadero.setText(list_parqueaderos.get(position).getNombre());
        holder.nombre_parqueadero.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistrarParqueoActivity.class);
                intent.putExtra("facultad_id", list_parqueaderos.get(position).getFacultad_id());
                intent.putExtra("parqueadero_id", list_parqueaderos.get(position).getId());
                Log.i("Accion", "Ir a otra activity");
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_parqueaderos.size();
    }
}