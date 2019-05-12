package net.rickiekarp.homeassistant.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.communication.controller.AddNotesController;
import net.rickiekarp.homeassistant.communication.vo.VONote;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnAddNotesResult;

/**
 * Created by sebastian on 06.12.17.
 */

public class AddNoteTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private IOnAddNotesResult uiCallback;
    private VONote note;
    private AppDatabase database;

    public AddNoteTask(SharedPreferences sp, IOnAddNotesResult uiCallback, VONote note, AppDatabase database) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.note = note;
        this.database = database;
    }

    @Override
    protected String doInBackground(Void... voids) {
        AddNotesController addNotesController = new AddNotesController(sp, uiCallback, note, database);
        addNotesController.start();
        return "Task executed";
    }
}
