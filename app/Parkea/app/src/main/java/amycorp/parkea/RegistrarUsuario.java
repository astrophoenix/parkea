package amycorp.parkea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button btn_guardar_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_apellido = (EditText) findViewById(R.id.txt_apellido);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_guardar_usuario = (Button) findViewById(R.id.btn_guardar_usuario);

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


        // Reset errors.
        //mEmailView.setError(null);
        //mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String nombre = txt_nombre.getText().toString();
        String apellido = txt_apellido.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            ////****
            APIService mApiService = Controller.getInterfaceService();
            Call<RespuestaAPIServidor> mService = mApiService.registrarUsuarioAPI(nombre, apellido, email, password);


            mService.enqueue(new Callback<RespuestaAPIServidor>() {
                @Override
                public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {

                    if(response.isSuccessful())
                    {
                        RespuestaAPIServidor mLoginObject = response.body();
                        String returnedResponse = mLoginObject.estado;
                        //showProgress(false);
                        if(returnedResponse.trim().equals("1")){
                            Intent inicioIntent = new Intent(getApplicationContext(), InicioActivity.class);
                            startActivity(inicioIntent);
                            // redirect to Main Activity page
                            //Intent facultadesIntent = new Intent(RegistrarUsuario.this, RegistrarParqueoActivity.class);
                            //startActivity(facultadesIntent);
//                            Toast.makeText(RegistrarUsuario.this, "Registrado OK", Toast.LENGTH_LONG).show();
                        }else if(returnedResponse.trim().equals("0")){
                            // use the registration button to register
                            //failedLoginMessage.setText(getResources().getString(R.string.registration_message));
                            Toast.makeText(RegistrarUsuario.this, "Usuario No registrado", Toast.LENGTH_LONG).show();
                            //mPasswordView.requestFocus();
                        }
                    }
                    else {
                        Log.e("Error Code", String.valueOf(response.code()));
                        Log.e("Error Body", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                    call.cancel();
                    Log.d("ERROR1", t.getMessage());
                    Log.i("ERROR2",t.getCause()+"");
                    //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
                }
            });
            ////****

        }
    }

}
