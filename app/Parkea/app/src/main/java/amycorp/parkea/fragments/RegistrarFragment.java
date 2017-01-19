package amycorp.parkea.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.PrincipalActivity;
import amycorp.parkea.R;
import amycorp.parkea.models.Facultad;
import amycorp.parkea.models.Global;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.models.Placa;
import amycorp.parkea.models.RespuestaAPIServidor;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn;

    private OnFragmentInteractionListener mListener;

    private Spinner spinnerFacultades;
    private Spinner spinnerParqueaderos;
    private Spinner spinnerPlacas;
    private Button btnRegistrar;
    private Double latitude = Double.valueOf(0);
    private Double longitude = Double.valueOf(0);
    Integer FACULTAD_ID = 0;
    Integer PARQUEADERO_ID = 0;
    Context thiscontext;

    public RegistrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarFragment newInstance(String param1, String param2) {
        RegistrarFragment fragment = new RegistrarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.fragment_registrar, container, false);
        thiscontext = container.getContext();

        spinnerFacultades = (Spinner) view.findViewById(R.id.spn_facultades);
        spinnerParqueaderos = (Spinner) view.findViewById(R.id.spn_parqueaderos);
        spinnerPlacas = (Spinner) view.findViewById(R.id.spn_placas);
        btnRegistrar = (Button) view.findViewById(R.id.btn_registrar);
        Bundle bundle = getArguments();
        if (bundle != null) {
            FACULTAD_ID = bundle.getInt("facultad_id");
            PARQUEADERO_ID = bundle.getInt("parqueadero_id");
        }
        //Carga las Facultades y Placas en los spinners
        obtenerFacultades();
        obtenerPlacasXPersona(Global.usuario_id);

        // Se bloquean los combos facultad y parqueo
        spinnerFacultades.setEnabled(false);
        spinnerFacultades.setClickable(false);
        spinnerParqueaderos.setEnabled(false);
        spinnerParqueaderos.setClickable(false);

        //Obtener Localización GPS
        LocationManager lm = (LocationManager)thiscontext.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }else{
            Toast.makeText(thiscontext, "Debe activar el gps", Toast.LENGTH_LONG).show();
        }


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registrarParqueoPersona();
            }
        });

        return view;

    }

    private void obtenerFacultades() {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<Facultad>> mService = mApiService.obtenerFacultadesAPI();
        mService.enqueue(new Callback<List<Facultad>>() {
            @Override
            public void onResponse(Call<List<Facultad>> call, Response<List<Facultad>> response) {
                if (response.isSuccessful()) {

                    List<Facultad> facultades = response.body();

                    final ArrayAdapter<Facultad> spinnerAdapter = new ArrayAdapter<Facultad>(
                            thiscontext,android.R.layout.simple_spinner_item, facultades){

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            // Set the Text color
                            tv.setTextColor(Color.parseColor("#222222"));

                            return view;
                        }
                    };

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
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#222222"));

                        }

                        public void onNothingSelected(AdapterView<?> parent) { }
                    });


                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facultad>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
                //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
            }


        });
    }


    private void obtenerParqueaderosXFacultad(Integer facultad_id ) {

        APIService mApiService = Controller.getInterfaceService();
        Call<List<Parqueadero>> mService = mApiService.obtenerParqueaderosXFacultadAPI(facultad_id);
        mService.enqueue(new Callback<List<Parqueadero>>() {
            @Override
            public void onResponse(Call<List<Parqueadero>> call, Response<List<Parqueadero>> response) {
                if (response.isSuccessful()) {

                    List<Parqueadero> parqueaderos = response.body();
                    //ArrayAdapter<Parqueadero> spinnerParqueaderosAdapter = new ArrayAdapter<Parqueadero>(getApplicationContext(), android.R.layout.simple_spinner_item, parqueaderos);
                    final ArrayAdapter<Parqueadero> spinnerParqueaderosAdapter = new ArrayAdapter<Parqueadero>(thiscontext,android.R.layout.simple_spinner_item, parqueaderos){

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            // Set the Text color
                            tv.setTextColor(Color.parseColor("#222222"));

                            return view;
                        }
                    };
                    spinnerParqueaderosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerParqueaderos.setAdapter(spinnerParqueaderosAdapter);
                    int i;
                    for (i = 0; i < parqueaderos.size(); i++) {
                        Integer parqueaid = parqueaderos.get(i).getId();
                        if ( parqueaid.toString().trim() == String.valueOf(PARQUEADERO_ID)){
                            spinnerParqueaderos.setSelection(i);
                            //((TextView) spinnerParqueaderos.getChildAt(0)).setTextColor(Color.parseColor("#222222"));
                        }

                    }

                    spinnerParqueaderos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Parqueadero>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
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
                    //ArrayAdapter<Placa> spinnerPlacasAdapter = new ArrayAdapter<Placa>(getApplicationContext(), android.R.layout.simple_spinner_item, placas);

                    //ArrayAdapter<Parqueadero> spinnerParqueaderosAdapter = new ArrayAdapter<Parqueadero>(getApplicationContext(), android.R.layout.simple_spinner_item, parqueaderos);
                    final ArrayAdapter<Placa> spinnerPlacasAdapter = new ArrayAdapter<Placa>(thiscontext,android.R.layout.simple_spinner_item, placas){

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
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Placa>> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }


        });
    }

    private void registrarParqueoPersona() {

        Parqueadero parqueadero = (Parqueadero)spinnerParqueaderos.getItemAtPosition(spinnerParqueaderos.getSelectedItemPosition());
        Integer parqueadero_id = parqueadero.getId();

        Placa placa = (Placa) spinnerPlacas.getItemAtPosition(spinnerPlacas.getSelectedItemPosition());
        String placa_nombre = placa.getNombre();

        //Log.e("longitude", String.valueOf(longitude));
        //Log.e("latitude", String.valueOf(latitude));
        APIService mApiService = Controller.getInterfaceService();
        Call<RespuestaAPIServidor> mService = mApiService.registrarParqueoPersonaAPI(parqueadero_id, placa_nombre, longitude, latitude, Global.usuario_id);

        mService.enqueue(new Callback<RespuestaAPIServidor>() {
            @Override
            public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {
                if (response.isSuccessful()) {
                    RespuestaAPIServidor r = response.body();
                    String returnedResponse = r.estado;
                    if(returnedResponse.trim().equals("1")){
                        Toast.makeText(thiscontext, "Registro Exitoso", Toast.LENGTH_LONG).show();
                        Intent inicioIntent = new Intent(thiscontext, PrincipalActivity.class);
                        startActivity(inicioIntent);
                    }else {
                        Toast.makeText(thiscontext, "Error al intentar registrar parqueo, verificar conexión", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                //call.cancel();
                //Log.d("ERROR1", t.getMessage());
                //Log.i("ERROR2", t.getCause() + "");
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
            }


        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
