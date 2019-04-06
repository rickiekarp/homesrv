package net.rickiekarp.shoppinghelper.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.shoppinghelper.communication.controller.RemoveNotesController;
import net.rickiekarp.shoppinghelper.db.AppDatabase;
import net.rickiekarp.shoppinghelper.interfaces.IOnRemoveNoteResult;

/**
 * Created by sebastian on 06.12.17.
 */

public class RemoveNoteTask extends AsyncTask<Void, Void, String> {

    SharedPreferences sp;
    AppDatabase database;
    IOnRemoveNoteResult uiCallback;
    int id;

    public RemoveNoteTask(SharedPreferences sp, IOnRemoveNoteResult uiCallback, AppDatabase database, int id) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.database = database;
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {

        RemoveNotesController removeNotesController = new RemoveNotesController(sp, uiCallback, database, id);
        removeNotesController.start();
        return "Task executed";
    }
}
