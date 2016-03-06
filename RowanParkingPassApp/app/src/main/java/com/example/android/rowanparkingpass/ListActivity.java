package com.example.android.rowanparkingpass;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class ListActivity extends Activity {

    public void buildListView(List<Object> list, int layout){
        /**
         *
         *
        ListView listView = (ListView) findViewById(R.id.events_list_view);
        final ListViewArrayAdapter adapter = new List(list, this, layout);
        listView.setAdapter(adapter);
        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
                Intent intent = new Intent(CalendarActivity.this, EventInfo.class);
                intent.putExtra("date", month + "/" + day + "/" + year);
                SELECTED_EVENET = (Event) adapter.getItem(position);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
         */

    }
}
