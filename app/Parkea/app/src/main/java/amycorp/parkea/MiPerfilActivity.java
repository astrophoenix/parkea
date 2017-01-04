package amycorp.parkea;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;

import amycorp.parkea.models.Global;
import amycorp.parkea.models.Placa;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.models.Usuario;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilActivity extends AppCompatActivity {
    private TextView lblnombres_apellido;
    private TextView lblcorreo;
    private Spinner spinnerPlacas;
    private Button btnAgregarPlaca;
    private EditText txtPlaca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        lblnombres_apellido = (TextView) findViewById(R.id.lblnombre);
        lblcorreo = (TextView) findViewById(R.id.lblemail);
        spinnerPlacas = (Spinner) findViewById(R.id.spn_placas);
        btnAgregarPlaca = (Button) findViewById(R.id.btnAgregarPlaca);
        txtPlaca = (EditText) findViewById(R.id.txt_placa);

        obtenerPlacasXPersona(Global.usuario_id);
        obtenerDatosUsuario(Global.usuario_id);


        btnAgregarPlaca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txt_placa = txtPlaca.getText().toString();
                agregarPlacaUsuario(Global.usuario_id, txt_placa);
            }
        });

    }


    private void obtenerDatosUsuario(Integer usuario_id)
    {
        APIService mApiService = Controller.getInterfaceService();
        Call<Usuario> mService = mApiService.obtenerUsuarioAPI(usuario_id);

        mService.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                //Respuesta Exitosa del Servidor
                if(response.isSuccessful())
                {
                    Usuario u = response.body();
                    lblnombres_apellido.setText(String.valueOf(u.getNombre()) + " " + String.valueOf(u.getApellido()) );
                    lblcorreo.setText(String.valueOf(u.getCorreo()));
                }
                else {
                    Log.e("Error Code", String.valueOf(response.code()));

                    Toast.makeText(getApplicationContext(), String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
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
                    final ArrayAdapter<Placa> spinnerPlacasAdapter = new ArrayAdapter<Placa>(getApplicationContext(),android.R.layout.simple_spinner_item, placas){

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            // Set the Text color
                            tv.setTextColor(Color.parseColor("#222222"));

                            return view;
                        }
                    };
                    spinnerPlacasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPlacas.setAdapter(spinnerPlacasAdapter);
                    spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#222222"));
                        }

                        public void onNothingSelected(AdapterView<?> parent) { }
                    });

                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Placa>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }


        });
    }


    private void agregarPlacaUsuario(Integer usuario_id, String placa)
    {
        APIService mApiService = Controller.getInterfaceService();
        Call<RespuestaAPIServidor> mService = mApiService.registrarPlacaUsuarioAPI(usuario_id, placa);

        mService.enqueue(new Callback<RespuestaAPIServidor>() {
            @Override
            public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {

                //Respuesta Exitosa del Servidor
                if(response.isSuccessful())
                {
                    RespuestaAPIServidor r = response.body();
                    String returnedResponse = r.estado;
                    if(returnedResponse.trim().equals("1")){
                        Toast.makeText(getApplicationContext(), "Placa registada Exitosamente.", Toast.LENGTH_LONG).show();
                        obtenerPlacasXPersona(Global.usuario_id);
                        txtPlaca.setText("");
                    }else if (returnedResponse.trim().equals("-1")) {
                        Toast.makeText(getApplicationContext(), "Placa ya existe para esta persona.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Placa No registada.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
