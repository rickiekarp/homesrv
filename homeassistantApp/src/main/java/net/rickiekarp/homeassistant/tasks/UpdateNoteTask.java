package net.rickiekarp.homeassistant.tasks;

import android.os.AsyncTask;

import net.rickiekarp.homeassistant.communication.controller.UpdateNotesController;
import net.rickiekarp.homeassistant.communication.vo.VONote;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnUpdateNotesResult;

/**
 * Created by sebastian on 06.12.17.
 */

public class UpdateNoteTask extends AsyncTask<Void, Void, String> {
    String token;
    VONote note;
    AppDatabase database;
    IOnUpdateNotesResult uiCallback;

    public UpdateNoteTask(String token, IOnUpdateNotesResult uiCallback, VONote note, AppDatabase database) {
        this.token = token;
        this.note = note;
        this.database = database;
        this.uiCallback = uiCallback;
    }
    @Override
    protected String doInBackground(Void... voids) {
        UpdateNotesController updateNotesController = new UpdateNotesController(token, uiCallback, note, database);
        updateNotesController.start();
        return "Task executed";
    }
}
