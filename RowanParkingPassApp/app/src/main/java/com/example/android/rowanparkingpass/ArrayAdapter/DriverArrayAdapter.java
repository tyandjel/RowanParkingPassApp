package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class DriverArrayAdapter extends BaseAdapter {
    LayoutInflater myInflator;
    private List<Driver> drivers = new ArrayList<>();
    private Context ctxt;

    public DriverArrayAdapter(List<Driver> l, Context c) {
        ctxt = c;
        makeDriversList(l);
        myInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void makeDriversList(List<Driver> d) {
        drivers.add(0, null); // adds empty place holder to position 0
        drivers.addAll(d);
    }

    @Override
    public int getCount() {
        return drivers.size();
    }

    @Override
    public Object getItem(int position) {
        return drivers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = myInflator.inflate(R.layout.view_driver, parent, false);
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
