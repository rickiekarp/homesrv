package net.rickiekarp.reddit.search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.rickiekarp.reddit.R;
import net.rickiekarp.reddit.common.Constants;

public class RedditSearchActivity extends Activity implements OnClickListener, OnItemSelectedListener,OnKeyListener {

    private EditText searchText;
    private String mSort = Constants.DEFAULT_SEARCH_SORT; //default to show most relevant results first

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_view);

        Spinner sortBy = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.search_results_sorting, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(adapter);
        sortBy.setOnItemSelectedListener(this);

        Button btn = (Button) findViewById(R.id.searchButton);
        btn.setOnClickListener(this);

        searchText = (EditText) findViewById(R.id.searchText);
        searchText.setOnKeyListener(this);
    }

    private void activityDone()
    {
        Intent intent = new Intent();
        intent.putExtra("searchurl", "search");
        intent.putExtra("query", searchText.getText().toString());
        intent.putExtra("sort", mSort);
        setResult(RESULT_OK, intent);
        finish();
    }

    //event handlers

    //not sure this is needed
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER)
        {
            if(event.getAction() == KeyEvent.ACTION_UP)
                activityDone();
            return true;
        }
        else
            return false;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mSort = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    public void onClick(View v) {
        activityDone();
    }

}
