package com.example.sebastian.projektapp.communication.controller;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.sebastian.projektapp.communication.ApiInterfaces;
import com.example.sebastian.projektapp.communication.vo.VOCredentials;
import com.example.sebastian.projektapp.communication.vo.VOToken;
import com.example.sebastian.projektapp.interfaces.IOnGetTokenResult;
import com.example.sebastian.projektapp.interfaces.IRunController;
import com.example.sebastian.projektapp.preferences.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sebastian.projektapp.Constants.URL.BASE_URL_LOGIN;

/**
 * Created by sebastian on 15.11.17.
 */

public class TokenController implements Callback<VOToken>, IRunController {

    private SharedPreferences sp;
    private VOCredentials credentials;
    private IOnGetTokenResult uiCallback;
    private String username, password;

    public TokenController(SharedPreferences sp, IOnGetTokenResult uiCallback, String username, String password) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.username = username;
        this.password = password;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.LoginApi api = retrofit.create(ApiInterfaces.LoginApi.class);

        credentials = new VOCredentials(username, password);
        Call<VOToken> call = api.doGetToken(credentials);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<VOToken> call, @NonNull Response<VOToken> response) {
        if (response.isSuccessful()) {
            // -> save token to preferences
            VOToken vo = response.body();
            if (vo != null) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Token.KEY, vo.getToken());
                editor.commit();

                uiCallback.onGetTokenSuccess();
            }
        } else {
            uiCallback.onGetTokenError();
        }
    }

    @Override
    public void onFailure(Call<VOToken> call, Throwable t) {
        t.printStackTrace();
    }
}
