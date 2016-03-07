package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class PassArrayAdapter extends BaseAdapter {
    LayoutInflater myInflater;
    private List<Pass> passes = new ArrayList<>();
    private Context ctxt;

    public PassArrayAdapter(List<Pass> l, Context c) {
        makePassesList(l);
        ctxt = c;
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * This is for creating the content for a list of Passes in listview
     */
    public void makePassesList(List<Pass> p) {
        passes.add(new Pass()); // adds empty place holder to position 0
        passes.addAll(p);
    }

    @Override
    public int getCount() {
        return passes.size();
    }

    @Override
    public Object getItem(int position) {
        return passes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView =myInflater.inflate(R.layout.view_recent_pass, parent, false);
        }

        TextView newPass = (TextView) convertView.findViewById(R.id.new_pass_text_view);
        TextView driverName = (TextView) convertView.findViewById(R.id.driver_text_view);
        TextView address = (TextView) convertView.findViewById(R.id.address_text_view);
        TextView townCity = (TextView) convertView.findViewById(R.id.town_city_text_view);
        TextView car = (TextView) convertView.findViewById(R.id.car_text_view);
        TextView plate = (TextView) convertView.findViewById(R.id.plate_text_view);
        if (position == 0) {
            newPass.setText("+ Create New Pass");
            driverName.setText("");
            address.setText("");
            townCity.setText("");
            car.setText("");
            plate.setText("");
        } else {
            newPass.setText("");
            Pass cPass = passes.get(position);
            driverName.setText(cPass.getDriver().getName());
            address.setText(cPass.getDriver().getStreet());
            townCity.setText(cPass.getDriver().getTown() + " " + cPass.getDriver().getState() + ", " + cPass.getDriver().getZipCode());
            car.setText(cPass.getVehicle().getYear() + " " + cPass.getVehicle().getMake() + " " + cPass.getVehicle().getModel() + " " + cPass.getVehicle().getColor());
            plate.setText(cPass.getVehicle().getVehicleState() + " " + cPass.getVehicle().getLicensePlate());
        }
        return convertView;
    }
}