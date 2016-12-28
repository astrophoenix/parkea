package amycorp.parkea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;

import amycorp.parkea.Adapters.FacultadAdaptador;
import amycorp.parkea.Adapters.ParqueaderoAdaptador;
import amycorp.parkea.models.Facultad;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.models.Placa;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarParqueoActivity extends AppCompatActivity {
    private Spinner spinnerFacultades;
    private Spinner spinnerParqueaderos;
    private Spinner spinnerPlacas;
    private Button btnRegistrar;
    Integer FACULTAD_ID = 0;
    Integer PARQUEADERO_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_parqueo);
        spinnerFacultades = (Spinner) findViewById(R.id.spn_facultades);
        spinnerParqueaderos = (Spinner) findViewById(R.id.spn_parqueaderos);
        spinnerPlacas = (Spinner) findViewById(R.id.spn_placas);
        btnRegistrar = (Button) findViewById(R.id.btn_registrar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FACULTAD_ID = bundle.getInt("facultad_id");
            PARQUEADERO_ID = bundle.getInt("parqueadero_id");
        }
        obtenerFacultades();
        obtenerPlacasXPersona(28); //id quemado temporalmente




        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registrarParqueoPersona();
            }
        });

    }

    private void obtenerFacultades() {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Facultad>> mService = mApiService.obtenerFacultadesAPI();
        mService.enqueue(new Callback<List<Facultad>>() {
            @Override
            public void onResponse(Call<List<Facultad>> call, Response<List<Facultad>> response) {
                if (response.isSuccessful()) {

                    List<Facultad> facultades = response.body();

                    ArrayAdapter<Facultad> spinnerAdapter = new ArrayAdapter<Facultad>(getApplicationContext(), android.R.layout.simple_spinner_item, facultades);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFacultades.setAdapter(spinnerAdapter);
                    int i;
                    for (i = 0; i < facultades.size(); i++) {
                        Integer facultid = facultades.get(i).getId();
                        if ( facultid.toString().trim() == String.valueOf(FACULTAD_ID))
                            spinnerFacultades.setSelection(i);
                    }

                    spinnerFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            Facultad selectedItem = (Facultad)parent.getItemAtPosition(position);
                            Integer facultad_id = selectedItem.getId();
                            obtenerParqueaderosXFacultad(facultad_id);
                        }

                        public void onNothingSelected(AdapterView<?> parent) { }
                    });


                } else {
                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Facultad>> call, Throwable t) {
                //call.cancel();
                Log.d("ERROR1", t.getMessage());
                Log.i("ERROR2", t.getCause() + "");
                //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
            }


        });
    }


    private void obtenerParqueaderosXFacultad(Integer facultad_id ) {
        Log.d("AAA", "Carga Parqueos" );
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Parqueadero>> mService = mApiService.obtenerParqueaderosXFacultadAPI(facultad_id);
        mService.enqueue(new Callback<List<Parqueadero>>() {
            @Override
            public void onResponse(Call<List<Parqueadero>> call, Response<List<Parqueadero>> response) {
                if (response.isSuccessful()) {

                    List<Parqueadero> parqueaderos = response.body();
                    ArrayAdapter<Parqueadero> spinnerParqueaderosAdapter = new ArrayAdapter<Parqueadero>(getApplicationContext(), android.R.layout.simple_spinner_item, parqueaderos);
                    spinnerParqueaderosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerParqueaderos.setAdapter(spinnerParqueaderosAdapter);
                    int i;
                    for (i = 0; i < parqueaderos.size(); i++) {
                        Integer parqueaid = parqueaderos.get(i).getId();
                        if ( parqueaid.toString().trim() == String.valueOf(PARQUEADERO_ID))
                            spinnerParqueaderos.setSelection(i);
                    }

                } else {
                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Parqueadero>> call, Throwable t) {
                //call.cancel();
                Log.d("ERROR1", t.getMessage());
                Log.i("ERROR2", t.getCause() + "");
                //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
            }


        });
    }


    private void obtenerPlacasXPersona(Integer persona_id) {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Placa>> mService = mApiService.obtenerPlacasXPersonaAPI(persona_id);
        mService.enqueue(new Callback<List<Placa>>() {
            @Override
            public void onResponse(Call<List<Placa>> call, Response<List<Placa>> response) {
                if (response.isSuccessful()) {
                    List<Placa> placas = response.body();
                    ArrayAdapter<Placa> spinnerPlacasAdapter = new ArrayAdapter<Placa>(getApplicationContext(), android.R.layout.simple_spinner_item, placas);
                    spinnerPlacasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPlacas.setAdapter(spinnerPlacasAdapter);
                } else {
                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Placa>> call, Throwable t) {
                //call.cancel();
                Log.d("ERROR1", t.getMessage());
                Log.i("ERROR2", t.getCause() + "");
                //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
            }


        });
    }

    private void registrarParqueoPersona() {

        Parqueadero parqueadero = (Parqueadero)spinnerParqueaderos.getItemAtPosition(spinnerParqueaderos.getSelectedItemPosition());
        Integer parqueadero_id = parqueadero.getId();

        Placa placa = (Placa) spinnerPlacas.getItemAtPosition(spinnerPlacas.getSelectedItemPosition());
        String placa_nombre = placa.getNombre();

        APIService mApiService = Controller.getInterfaceService();
        Call<RespuestaAPIServidor> mService = mApiService.registrarParqueoPersonaAPI(parqueadero_id, placa_nombre);

        mService.enqueue(new Callback<RespuestaAPIServidor>() {
            @Override
            public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {
                if (response.isSuccessful()) {
                    RespuestaAPIServidor r = response.body();
                    String returnedResponse = r.estado;
                } else {
                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                //call.cancel();
                Log.d("ERROR1", t.getMessage());
                Log.i("ERROR2", t.getCause() + "");
                //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
            }


        });
    }

}