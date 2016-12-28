package amycorp.parkea.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Controller {

    public static APIService getInterfaceService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final APIService mInterfaceService = retrofit.create(APIService.class);
        return mInterfaceService;
    }
}

