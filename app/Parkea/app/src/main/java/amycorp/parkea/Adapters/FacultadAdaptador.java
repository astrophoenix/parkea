package amycorp.parkea.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amycorp.parkea.ParqueaderosFacultadActivity;
import amycorp.parkea.R;
import amycorp.parkea.models.Facultad;


public class FacultadAdaptador extends RecyclerView.Adapter<FacultadAdaptador.ViewHolder>
{

    private List<Facultad> list_facultades;

    public FacultadAdaptador(List<Facultad> list_facultades) {
        this.list_facultades = list_facultades;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nombre_facultad;


        public ViewHolder(final View itemView)
        {
            super(itemView);
            nombre_facultad = (TextView) itemView.findViewById(R.id.nombre_facultad);
        }


    }

    @Override
    public FacultadAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_facultad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FacultadAdaptador.ViewHolder holder, final int position) {

        holder.nombre_facultad.setText(list_facultades.get(position).getNombre());
        holder.nombre_facultad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ParqueaderosFacultadActivity.class);
                intent.putExtra("facultad_id", list_facultades.get(position).getId());
                Log.i("CustomAdapter", String.valueOf(" id:"+ list_facultades.get(position).getId() ));
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_facultades.size();
    }
}