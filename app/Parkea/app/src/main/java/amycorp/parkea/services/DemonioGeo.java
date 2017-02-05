package amycorp.parkea.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import amycorp.parkea.models.*;

public class DemonioGeo extends Service {
    private static final String TAG = "TEST";
    Geolocalizacion geo_posicion = new Geolocalizacion();

    public DemonioGeo() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");
        geo_posicion.execute();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
        super.onDestroy();
        geo_posicion.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class Geolocalizacion extends AsyncTask<String, String, String> {

        //private Double latitud = Double.valueOf(0);
        //private Double longitud = Double.valueOf(0);
        private boolean cent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dateFormat = new SimpleDateFormat("HH:mm:ss");
            cent = true;
        }

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                //date = dateFormat.format(new Date());
                try {
                    publishProgress();
                    // Stop 5s
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            APIService mApiService = Controller.getInterfaceService();
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null){
                Global.longitud = location.getLongitude();
                Global.latitud = location.getLatitude();
            }

            Log.d("LON", String.valueOf(Global.longitud));
            Log.d("LAT", String.valueOf(Global.latitud));

            Call<RespuestaAPIServidor> mService = mApiService.verificarUsuarioAreaParqueadero(Global.usuario_id, Global.longitud, Global.latitud);
            mService.enqueue(new Callback<RespuestaAPIServidor>() {
                @Override
                public void onResponse(Call<RespuestaAPIServidor> call, Response<RespuestaAPIServidor> response) {

                    if(response.isSuccessful())
                    {
                        RespuestaAPIServidor r = response.body();
                        String returnedResponse = r.estado;
                        if(returnedResponse.trim().equals("0")){
                            Global.en_area = false;
                            Toast.makeText(getApplicationContext(), "long:" + String.valueOf(Global.longitud)+ " - lat: "+ String.valueOf(Global.latitud) + " NO ESTA EN PARQUEADERO FIEC", Toast.LENGTH_LONG).show();
                        }else {
                            Global.en_area = true;
                            Toast.makeText(getApplicationContext(), "long:" + String.valueOf(Global.longitud)+ " - lat: "+ String.valueOf(Global.latitud) + " SI ESTA EN PARQUEADERO FIEC", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        //Log.e("Error Code", String.valueOf(response.code()));
                        //Log.e("Error Body", response.errorBody().toString());
                        Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaAPIServidor> call, Throwable t) {
                    //call.cancel();
                    //Log.d("ERROR1", t.getMessage());
                    //Log.i("ERROR2",t.getCause()+"");
                    //Toast.makeText(getApplicationContext(), "Conexión con el servidor no establecida.", Toast.LENGTH_LONG).show();
                    //Toast.makeText(LoginActivity.this, "No tiene permisos para el Servicio de Internet", Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }
}
