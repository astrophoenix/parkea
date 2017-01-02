package amycorp.parkea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.Adapters.FacultadAdaptador;
import amycorp.parkea.models.Facultad;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultadesActivity extends AppCompatActivity {
    private RecyclerView rv;
    private FacultadAdaptador facultad_adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultades);

        rv = (RecyclerView) findViewById(R.id.lista_facultades);
        obtenerFacultades();

    }

    private void obtenerFacultades() {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Facultad>> mService = mApiService.obtenerFacultadesAPI();
        mService.enqueue(new Callback<List<Facultad>>() {
            @Override
            public void onResponse(Call<List<Facultad>> call, Response<List<Facultad>> response) {
                if (response.isSuccessful()) {

                    List<Facultad> facultades = response.body();
                    facultad_adaptador = new FacultadAdaptador(facultades);

                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setHasFixedSize(false);
                    rv.setLayoutManager(layoutManager);
                    rv.setAdapter(facultad_adaptador);

                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facultad>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }


        });
    }

}
