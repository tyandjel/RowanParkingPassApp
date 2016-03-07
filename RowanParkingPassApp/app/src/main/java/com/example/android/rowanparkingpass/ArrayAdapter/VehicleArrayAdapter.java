package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class VehicleArrayAdapter extends BaseAdapter {
    LayoutInflater myInflator;
    private List<Vehicle> vehicles = new ArrayList<>();
    private Context ctxt;

    public VehicleArrayAdapter(List<Vehicle> l, Context c) {
        ctxt = c;
        makeVehiclesList(l);
        myInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * This is for creating the content for a list of drivers in listview
     */
    public void makeVehiclesList(List<Vehicle> v) {
        vehicles.add(0, null); // adds empty place holder to position 0
        vehicles.addAll(v);
    }


    @Override
    public int getCount() {
        return vehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return vehicles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = myInflator.inflate(R.layout.view_vehicle, parent, false);
        }

        TextView newVehicle = (TextView) convertView.findViewById(R.id.new_vehicle_text_view);
        TextView carText = (TextView) convertView.findViewById(R.id.car_text_view);
        TextView plateText = (TextView) convertView.findViewById(R.id.plate_text_view);

        if (position == 0) {
            newVehicle.setText("+ Create New Vehicle");
            carText.setText("");
            plateText.setText("");
        } else {
            newVehicle.setText("");
            Vehicle cVehicle = vehicles.get(position);
            carText.setText(cVehicle.getYear() + " " + cVehicle.getMake() + " " + cVehicle.getModel() + " " + cVehicle.getColor());
            plateText.setText(cVehicle.getLicensePlate());
        }
        return convertView;
    }
}