package amycorp.parkea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.Adapters.ParqueaderoAdaptador;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParqueaderosFacultadActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ParqueaderoAdaptador parqueadero_adaptador;
    Integer FACULTAD_ID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parqueaderos_facultad);
        rv = (RecyclerView) findViewById(R.id.lista_parqueaderos);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FACULTAD_ID = bundle.getInt("facultad_id");
        }
        obtenerParqueaderosXFacultad(FACULTAD_ID);

    }

    private void obtenerParqueaderosXFacultad(Integer facultad_id) {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Parqueadero>> mService = mApiService.obtenerParqueaderosXFacultadAPI(facultad_id);
        mService.enqueue(new Callback<List<Parqueadero>>() {
            @Override
            public void onResponse(Call<List<Parqueadero>> call, Response<List<Parqueadero>> response) {
                if (response.isSuccessful()) {

                    List<Parqueadero> parqueaderos = response.body();
                    parqueadero_adaptador = new ParqueaderoAdaptador(parqueaderos);

                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setLayoutManager(layoutManager);
                    rv.setHasFixedSize(false);
                    rv.setAdapter(parqueadero_adaptador);

                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Parqueadero>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }


        });
    }

}
