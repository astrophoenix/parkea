package amycorp.parkea;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.List;

import amycorp.parkea.fragments.ConsultarFragment;
import amycorp.parkea.fragments.MapaParqueosFragment;
import amycorp.parkea.fragments.NoticiasFragment;
import amycorp.parkea.fragments.ParqueaderosFacultadFragment;
import amycorp.parkea.fragments.ParqueoPersonaFragment;
import amycorp.parkea.fragments.RegistrarFragment;
import amycorp.parkea.fragments.ReportarFragment;
import amycorp.parkea.models.Global;
import amycorp.parkea.models.Placa;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.models.Usuario;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import amycorp.parkea.services.DemonioGeo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConsultarFragment.OnFragmentInteractionListener,
        RegistrarFragment.OnFragmentInteractionListener,
        NoticiasFragment.OnFragmentInteractionListener,
        ReportarFragment.OnFragmentInteractionListener,
        ParqueoPersonaFragment.OnFragmentInteractionListener,
        ParqueaderosFacultadFragment.OnFragmentInteractionListener,
        MapaParqueosFragment.OnFragmentInteractionListener
        {

    private TextView lblnombres_apellido;
    private TextView lblnombres_apellido_header;
    private TextView lblcorreo_header;
    private TextView lblcorreo;
    private Spinner spinnerPlacas;
    private Button btnAgregarPlaca;
    private Button bntHistorialParqueos;
    private EditText txtPlaca;
    private RatingBar ratingReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TypefaceProvider.registerDefaultIconSets();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);



        lblnombres_apellido = (TextView) findViewById(R.id.lblnombre);
        lblcorreo = (TextView) findViewById(R.id.lblemail);
        lblnombres_apellido_header = (TextView) findViewById(R.id.nombres_usuarios);
        lblcorreo_header = (TextView) findViewById(R.id.correo_usuario);
        spinnerPlacas = (Spinner) findViewById(R.id.spn_placas);
        btnAgregarPlaca = (Button) findViewById(R.id.btnAgregarPlaca);
        bntHistorialParqueos = (Button) findViewById(R.id.bntHistorialParqueos);
        txtPlaca = (EditText) findViewById(R.id.txt_placa);
        ratingReporte = (RatingBar) findViewById(R.id.rtb_reportar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String menuFragment = getIntent().getStringExtra("menuFragment");
            Log.e("menuFRag", String.valueOf(menuFragment));
            if(menuFragment != null && menuFragment.equals("menu_recompensas"))
            {
                Fragment fragment = new NoticiasFragment();
                Bundle args = new Bundle();
                args.putString("tipo_noticia", "R");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).commit();
                getSupportActionBar().setTitle("Recompensas");


            }

            String from_login = getIntent().getStringExtra("from_login");
            if (from_login != null && from_login.equals("1"))
            {
                startService(new Intent(getApplicationContext(), DemonioGeo.class));
            }
        }


        obtenerPlacasXPersona(Global.usuario_id);
        obtenerDatosUsuario(Global.usuario_id);
        obtenerRatingReportesUsuario(Global.usuario_id);


        btnAgregarPlaca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txt_placa = txtPlaca.getText().toString();
                agregarPlacaUsuario(Global.usuario_id, txt_placa);
            }
        });

        //Validación de Placa
        final MaskedTextChangedListener listener = new MaskedTextChangedListener("[AAA]-[0000]", true, txtPlaca, null, null);
        txtPlaca.addTextChangedListener(listener);
        txtPlaca.setOnFocusChangeListener(listener);


        bntHistorialParqueos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new ParqueoPersonaFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).commit();
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
                    lblnombres_apellido_header.setText(String.valueOf(u.getNombre()) + " " + String.valueOf(u.getApellido()) );
                    lblcorreo_header.setText(String.valueOf(u.getCorreo()));
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

    private void obtenerPlacasXPersona(Integer persona_id)
    {
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
        Boolean error = false;
        String msj_error = "";

        txtPlaca.setError(null);
        if (placa.equals("")){
            error = true;
            msj_error = "Placa, no puede ser vacía";
        }else if (placa.length() < 7){
            error = true;
            msj_error = "Placa, formato incorrecta";
        }

        if (!error){
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
        }else{
            txtPlaca.setError(msj_error);
            txtPlaca.requestFocus();
        }

    }

    private void obtenerRatingReportesUsuario(Integer usuario_id)
    {
        APIService mApiService = Controller.getInterfaceService();
        Call<RespuestaAPIServidor> mService = mApiService.obtenerRatingUsuarioAPI(usuario_id);

        mService.enqueue(new Callback<RespuestaAPIServidor>() {
            @Override
            public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {

                //Respuesta Exitosa del Servidor
                if(response.isSuccessful())
                {
                    RespuestaAPIServidor r = response.body();
                    String returnedResponse = r.estado;
                    if(!returnedResponse.trim().equals("")){
                        ratingReporte.setRating(Float.parseFloat(returnedResponse));
                    }else{
                        ratingReporte.setRating(Float.parseFloat("0.0"));
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean FragmentTransaction = false;
        Fragment fragment = null;
        Bundle args = new Bundle();

        if (id == R.id.nav_mi_perfil) {
            Intent inicioIntent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(inicioIntent);
            FragmentTransaction = false;
        }if (id == R.id.nav_consular) {
            fragment = new ConsultarFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_registrar) {
            fragment = new RegistrarFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_reportar) {
            fragment = new ReportarFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_eventos) {
            fragment = new NoticiasFragment();
            FragmentTransaction = true;
            args.putString("tipo_noticia", "E");
            fragment.setArguments(args);
        } else if (id == R.id.nav_encuesta) {
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.stackoverflow.com/"));
            startActivity(viewIntent);
            FragmentTransaction = false;
        } else if (id == R.id.nav_recompensa) {
            fragment = new NoticiasFragment();
            args.putString("tipo_noticia", "R");
            fragment.setArguments(args);
            FragmentTransaction = true;
        } else if (id == R.id.nav_mapa_espol) {
            fragment = new MapaParqueosFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_salir) {
            stopService(new Intent(getApplicationContext(), DemonioGeo.class));
            FragmentTransaction = false;
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            System.exit(0);
        }

        if (FragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).addToBackStack(null).commit();
            btnAgregarPlaca.setOnClickListener(null);
            bntHistorialParqueos.setOnClickListener(null);
        }
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), DemonioGeo.class));
    }
}
