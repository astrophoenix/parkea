package amycorp.parkea.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.Adapters.ParqueaderoAdaptador;
import amycorp.parkea.Adapters.ParqueoPersonaAdaptador;
import amycorp.parkea.R;
import amycorp.parkea.models.Global;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.models.ParqueoPersona;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParqueoPersonaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParqueoPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParqueoPersonaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv;
    private ParqueoPersonaAdaptador parqueo_persona_adaptador;
    Context thiscontext;

    public ParqueoPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParqueoPersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParqueoPersonaFragment newInstance(String param1, String param2) {
        ParqueoPersonaFragment fragment = new ParqueoPersonaFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.fragment_parqueo_persona, container, false);
        thiscontext = container.getContext();
        rv = (RecyclerView) view.findViewById(R.id.lista_parqueos_persona);
        obtenerParqueosXPersona(Global.usuario_id);
        ((AppCompatActivity)thiscontext).getSupportActionBar().setTitle("Historial Parqueos");
        return view;

    }


    private void obtenerParqueosXPersona(Integer persona_id)
    {
        APIService mApiService = Controller.getInterfaceService();
        Call<List<ParqueoPersona>> mService = mApiService.obtenerHistorialXPersonaAPI(persona_id);
        mService.enqueue(new Callback<List<ParqueoPersona>>() {
            @Override
            public void onResponse(Call<List<ParqueoPersona>> call, Response<List<ParqueoPersona>> response) {
                if (response.isSuccessful()) {

                    List<ParqueoPersona> parqueos_persona = response.body();
                    parqueo_persona_adaptador = new ParqueoPersonaAdaptador(parqueos_persona);

                    final LinearLayoutManager layoutManager = new LinearLayoutManager(thiscontext);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setHasFixedSize(false);
                    rv.setLayoutManager(layoutManager);
                    rv.setAdapter(parqueo_persona_adaptador);

                } else {
                    //Log.e("Error Code", String.valueOf(response.code()));
                    //Log.e("Error Body", response.errorBody().toString());
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ParqueoPersona>> call, Throwable t) {
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
