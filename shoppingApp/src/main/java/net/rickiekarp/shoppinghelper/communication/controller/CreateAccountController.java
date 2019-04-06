package net.rickiekarp.shoppinghelper.communication.controller;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import net.rickiekarp.shoppinghelper.communication.ApiInterfaces;
import net.rickiekarp.shoppinghelper.communication.vo.VOCredentials;
import net.rickiekarp.shoppinghelper.communication.vo.VOToken;
import net.rickiekarp.shoppinghelper.interfaces.IOnCreateAccountResult;
import net.rickiekarp.shoppinghelper.interfaces.IRunController;
import net.rickiekarp.shoppinghelper.preferences.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.rickiekarp.shoppinghelper.Constants.URL.BASE_URL_LOGIN;

/**
 * Created by sebastian on 17.11.17.
 */

public class CreateAccountController implements Callback<VOToken>, IRunController {

    private SharedPreferences sp;
    private String username, password;
    private IOnCreateAccountResult taskCallback;

    public CreateAccountController(SharedPreferences sp, String username, String password, IOnCreateAccountResult taskCallback) {
        this.sp = sp;
        this.username = username;
        this.password = password;
        this.taskCallback = taskCallback;
    }

    @Override
    public void start() {
        VOCredentials credentials = new VOCredentials(username, password);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_LOGIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.LoginApi api = retrofit.create(ApiInterfaces.LoginApi.class);

        Call<VOToken> call = api.doCreateAccount(credentials);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<VOToken> call, @NonNull Response<VOToken> response) {
        if (response.code() == 200) {
            VOToken vo = response.body();
            if (vo != null) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Token.KEY, vo.getToken());
                editor.apply();

                taskCallback.onCreationSuccess();

            }
        } else {
            taskCallback.onCreationError();
        }
    }

    @Override
    public void onFailure(@NonNull Call<VOToken> call, @NonNull Throwable t) {
        t.printStackTrace();
        taskCallback.onCreationError();
    }
}
