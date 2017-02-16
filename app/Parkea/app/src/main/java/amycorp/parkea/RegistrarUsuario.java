package amycorp.parkea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.redmadrobot.inputmask.MaskedTextChangedListener;

import amycorp.parkea.models.Global;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarUsuario extends AppCompatActivity {

    private EditText txt_nombre;
    private EditText txt_apellido;
    private EditText txt_email;
    private EditText txt_password;
    private EditText txt_placa;
    private Button btn_guardar_usuario;
    //private static final String PLACA_MASK = "AAA 9999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_apellido = (EditText) findViewById(R.id.txt_apellido);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_placa = (EditText) findViewById(R.id.txt_placa);
        btn_guardar_usuario = (Button) findViewById(R.id.btn_guardar_usuario);


        //final MaskedTextChangedListener listener = new MaskedTextChangedListener("[AAA]-[0000]", true, txt_placa, null, null);
        //txt_placa.addTextChangedListener(listener);
        //txt_placa.setOnFocusChangeListener(listener);

//        txt_placa.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                txt_placa.setSelection(txt_placa.getText().length());
//                return false;
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_guardar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarNuevoUsuario();
            }
        });



    }

    private void guardarNuevoUsuario() {

        TextInputLayout txtobj1= null;
        txtobj1 = (TextInputLayout) txt_nombre.getParent();
        txtobj1.setError(null);

        txtobj1 = (TextInputLayout) txt_apellido.getParent();
        txtobj1.setError(null);

        txtobj1 = (TextInputLayout) txt_placa.getParent();
        txtobj1.setError(null);

        txtobj1 = (TextInputLayout) txt_email.getParent();
        txtobj1.setError(null);

        txtobj1 = (TextInputLayout) txt_password.getParent();
        txtobj1.setError(null);


        String nombre = txt_nombre.getText().toString();
        String apellido = txt_apellido.getText().toString();
        String placa = txt_placa.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (nombre.isEmpty()) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_nombre.getParent();
            txtobj.setError(getString(R.string.error_field_required));
            focusView = txtobj;
            cancel = true;
        }else if (apellido.isEmpty()) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_apellido.getParent();
            txtobj.setError(getString(R.string.error_field_required));
            focusView = txtobj;
            cancel = true;
        }else if (placa.length()>0  && placa.length() < 7) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_placa.getParent();
            txtobj.setError("Placa Incorrecta");
            focusView = txtobj;
            cancel = true;
        }else if (email.isEmpty()) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_email.getParent();
            txtobj.setError(getString(R.string.error_field_required));
            focusView = txtobj;
            cancel = true;
        } else if (!email.contains("@")) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_email.getParent();
            txtobj.setError(getString(R.string.error_invalid_email));
            focusView = txtobj;
            cancel = true;
        }else if (password.isEmpty()) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_password.getParent();
            txtobj.setError(getString(R.string.error_incorrect_password));
            focusView = txtobj;
            cancel = true;
        }else if (password.length() < 4) {
            focusView = null;
            TextInputLayout txtobj = (TextInputLayout) txt_password.getParent();
            txtobj.setError(getString(R.string.error_invalid_password));
            focusView = txtobj;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            APIService mApiService = Controller.getInterfaceService();
            if (placa.equals(""))
                placa = "---";

            Call<RespuestaAPIServidor> mService = mApiService.registrarUsuarioAPI(nombre, apellido, placa, email, password);
            mService.enqueue(new Callback<RespuestaAPIServidor>() {
                @Override
                public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {

                    if(response.isSuccessful())
                    {
                        RespuestaAPIServidor mLoginObject = response.body();
                        String returnedResponse = mLoginObject.estado;
                        //showProgress(false);
                        if(returnedResponse.trim().equals("0")) {
                            Toast.makeText(RegistrarUsuario.this, "No se ha podido registrar el usuario, ya existe", Toast.LENGTH_SHORT).show();
                        }else if(returnedResponse.trim().equals("-1")){
                            View focusView = null;
                            TextInputLayout txtobj = (TextInputLayout) txt_placa.getParent();
                            txtobj.setError("Placa no registrada en ATM");
                            focusView = txtobj;
                            focusView.requestFocus();
                        }else {
                            Global.usuario_id = Integer.valueOf(returnedResponse);
                            Toast.makeText(RegistrarUsuario.this, "Registro exitoso de usuario", Toast.LENGTH_SHORT).show();
                            Intent inicioIntent = new Intent(getApplicationContext(), PrincipalActivity.class);
                            inicioIntent.putExtra("from_login", "1");
                            startActivity(inicioIntent);
                            finish();
                        }
                    }
                    else {
                        //Log.e("Error Code", String.valueOf(response.code()));
                        //Log.e("Error Body", response.errorBody().toString());
                        Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                    //call.cancel();
                    //Log.d("ERROR1", t.getMessage());
                    //Log.i("ERROR2",t.getCause()+"");
                    Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

}
