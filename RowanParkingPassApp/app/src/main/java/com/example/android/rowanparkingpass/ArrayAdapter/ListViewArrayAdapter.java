package com.example.android.rowanparkingpass.ArrayAdapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Filterable;

/**
 * Created by Johnathan Saunders on 2/16/2016.
 * This class will be in charge of handling the content put into the activity_list_view
 * it will fullName activities screens 2,3,4,6 and 7 (home, driver, vehicle pages, View Drivers, and View Vehicles).
 */
public abstract class ListViewArrayAdapter extends BaseAdapter implements Filterable {

    private boolean hasLoaded = false;

    public void setHasLoaded(boolean b) {
        hasLoaded = b;
    }

    public View animateList(int pos, View convertView) {
        Animation animation = null;
        if (pos == 0) {// stops animation form effecting view in pos 0
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            // This creates a translation animation from bottom to the top
            animation = new TranslateAnimation(0, 0, 2000, 0);
        }

        //makes there a delay between views for translation speed.
        if (!hasLoaded) {
            animation.setDuration(500 + (pos * 100));
            convertView.startAnimation(animation);
        }
        return convertView;
    }


}
