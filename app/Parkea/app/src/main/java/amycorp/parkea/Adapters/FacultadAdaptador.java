package amycorp.parkea.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.ParqueaderosFacultadActivity;
import amycorp.parkea.PrincipalActivity;
import amycorp.parkea.R;
import amycorp.parkea.fragments.ParqueaderosFacultadFragment;
import amycorp.parkea.fragments.ParqueoPersonaFragment;
import amycorp.parkea.models.Facultad;


public class FacultadAdaptador extends RecyclerView.Adapter<FacultadAdaptador.ViewHolder>
{


    private List<Facultad> list_facultades;
    Context thiscontext;

    public FacultadAdaptador(List<Facultad> list_facultades) {
        this.list_facultades = list_facultades;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nombre_facultad;
        private TextView num_parqueos;


        public ViewHolder(final View itemView)
        {
            super(itemView);
            nombre_facultad = (TextView) itemView.findViewById(R.id.nombre_facultad);
            num_parqueos = (TextView) itemView.findViewById(R.id.num_parqueos);
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
        holder.num_parqueos.setText(String.valueOf(list_facultades.get(position).getNum_parqueos()) + " Parqueadeos");
        holder.nombre_facultad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thiscontext = v.getContext();
                if( list_facultades.get(position).getNombre().toString().equals("Fiec") ){
                    Fragment fragment = new ParqueaderosFacultadFragment();
                    Bundle args = new Bundle();
                    args.putInt("facultad_id", list_facultades.get(position).getId());
                    fragment.setArguments(args);
                    ((AppCompatActivity)thiscontext).getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).addToBackStack(null).commit();

                }else{
                    Toast.makeText(thiscontext, String.valueOf("Parqueaderos Cerrados"), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_facultades.size();
    }
}