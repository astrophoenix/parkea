package amycorp.parkea.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.R;
import amycorp.parkea.models.Noticia;
import amycorp.parkea.models.ParqueoPersona;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticiaAdaptador extends RecyclerView.Adapter<NoticiaAdaptador.ViewHolder>

{

    private List<Noticia> list_noticia;
    Context thiscontext;

    public NoticiaAdaptador(List<Noticia> list_noticia) {
        this.list_noticia = list_noticia;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView descripcion;
        private ImageView img_noticia;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            descripcion = (TextView) itemView.findViewById(R.id.list_desc_noticia);
            img_noticia = (ImageView) itemView.findViewById(R.id.list_img_noticia);
        }

    }

    @Override
    public NoticiaAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_noticia, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final NoticiaAdaptador.ViewHolder holder, final int position) {

        String noticia_descripcion = list_noticia.get(position).getDescripcion();
        String tipo_noticia = list_noticia.get(position).getTipo();

        if (tipo_noticia.equals("E"))
            holder.img_noticia.setImageResource(R.drawable.calendar);
        if (tipo_noticia.equals("R"))
            holder.img_noticia.setImageResource(R.drawable.bell);

        holder.descripcion.setText(noticia_descripcion);

    }

    @Override
    public int getItemCount() {
        return list_noticia.size();
    }
}