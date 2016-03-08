package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by John on 3/6/2016.
 */


public class DriverArrayAdapter extends BaseAdapter {

    private List<Driver> drivers = new ArrayList<>();

    private Context context;
    LayoutInflater myInflater;

    private int layout = R.layout.view_driver;// current layout to use

    /**
     * This  is for creating the content for a list of passes listview
     */
    public DriverArrayAdapter(List<Driver> l, Context c) {
        if (!l.isEmpty()) {
            makeDriversList(l);
        } else {
            makeDriversList(new ArrayList<Driver>());
        }
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * This is for creating the content for a list of vehicles in listview
     */
    public void makeDriversList(List<Driver> d) {
        drivers.add(0, null); // adds empty place holder to position 0
        drivers.addAll(d);
    }




    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return drivers.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return drivers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = myInflater.inflate(R.layout.view_driver, parent, false);
            }

        TextView newDriver = (TextView) convertView.findViewById(R.id.new_visitor_text_view);
        TextView driver = (TextView) convertView.findViewById(R.id.driver_text_view);
        TextView driverAddress = (TextView) convertView.findViewById(R.id.address_text_view);
        TextView driverTownCity = (TextView) convertView.findViewById(R.id.town_city_text_view);

        if (position == 0) {
            newDriver.setText("+ Create New Driver");
            driver.setText("");
            driverAddress.setText("");
            driverTownCity.setText("");
        } else {
            newDriver.setText("");
            Driver cDriver = drivers.get(position);
            driver.setText(cDriver.getName());
            driverAddress.setText(cDriver.getStreet());
            driverTownCity.setText(cDriver.getTown() + "," + cDriver.getState() + " " + cDriver.getZipCode());
        }

        return convertView;
    }
}
