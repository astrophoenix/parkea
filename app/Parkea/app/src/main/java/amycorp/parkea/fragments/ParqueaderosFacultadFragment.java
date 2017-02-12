package amycorp.parkea.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import amycorp.parkea.Adapters.ParqueaderoAdaptador;
import amycorp.parkea.R;
import amycorp.parkea.models.Parqueadero;
import amycorp.parkea.services.APIService;
import amycorp.parkea.services.Controller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParqueaderosFacultadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParqueaderosFacultadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParqueaderosFacultadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv;
    private ParqueaderoAdaptador parqueadero_adaptador;
    Integer FACULTAD_ID=0;
    Context thiscontext;

    public ParqueaderosFacultadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParqueaderosFacultadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParqueaderosFacultadFragment newInstance(String param1, String param2) {
        ParqueaderosFacultadFragment fragment = new ParqueaderosFacultadFragment();
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
        // Inflate the layout for this fragment
        final View view;
        view = inflater.inflate(R.layout.fragment_parqueaderos_facultad, container, false);
        thiscontext = container.getContext();
        rv = (RecyclerView) view.findViewById(R.id.lista_parqueaderos);
        Bundle bundle = getArguments();
        if (bundle != null) {
            FACULTAD_ID = bundle.getInt("facultad_id");
        }

        obtenerParqueaderosXFacultad(FACULTAD_ID);

        return view;
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

                    final LinearLayoutManager layoutManager = new LinearLayoutManager(thiscontext);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setLayoutManager(layoutManager);
                    rv.setHasFixedSize(false);
                    rv.setAdapter(parqueadero_adaptador);

                } else {
                    Toast.makeText(thiscontext, String.valueOf(response.errorBody().toString()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Parqueadero>> call, Throwable t) {
                Toast.makeText(thiscontext, "Conexión con el servidor no establecida.", Toast.LENGTH_SHORT).show();
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
