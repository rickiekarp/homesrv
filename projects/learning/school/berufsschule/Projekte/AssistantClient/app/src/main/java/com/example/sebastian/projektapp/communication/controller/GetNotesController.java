package com.example.sebastian.projektapp.communication.controller;

import android.content.SharedPreferences;

import com.example.sebastian.projektapp.utils.Util;
import com.example.sebastian.projektapp.communication.ApiInterfaces;
import com.example.sebastian.projektapp.communication.vo.VONotes;
import com.example.sebastian.projektapp.interfaces.IOnGetAllNotesResult;
import com.example.sebastian.projektapp.interfaces.IRunController;
import com.example.sebastian.projektapp.preferences.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sebastian.projektapp.Constants.URL.BASE_URL_APPSERVER;

/**
 * Created by sebastian on 22.11.17.
 */

public class GetNotesController implements Callback<List<VONotes>>, IRunController {

    private SharedPreferences sp;
    private IOnGetAllNotesResult uiCallback;

    public GetNotesController(SharedPreferences sp, IOnGetAllNotesResult uiCallback) {
        this.uiCallback = uiCallback;
        this.sp = sp;
    }

    @Override
    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_APPSERVER)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.NotesApi api = retrofit.create(ApiInterfaces.NotesApi.class);

        Call<List<VONotes>> call = api.getNotes(Util.generateToken(sp.getString(Token.KEY, "")));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<VONotes>> call, Response<List<VONotes>> response) {
        if (response.code() == 200) {
            List<VONotes> voList = response.body();
            uiCallback.onGetAllNotesSuccess(voList);
        } else {
            uiCallback.onGetAllNotesError();
        }
    }

    @Override
    public void onFailure(Call<List<VONotes>> call, Throwable t) {
        uiCallback.onGetAllNotesError();
    }
}
