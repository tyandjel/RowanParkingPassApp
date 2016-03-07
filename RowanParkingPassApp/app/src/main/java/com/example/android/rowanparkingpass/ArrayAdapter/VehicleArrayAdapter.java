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
<<<<<<< HEAD
    LayoutInflater myInflator;
    private List<Vehicle> vehicles = new ArrayList<>();
    private Context ctxt;

    public VehicleArrayAdapter(List<Vehicle> l, Context c) {
        ctxt = c;
        makeVehiclesList(l);
        myInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

=======

    private List<Vehicle> vehicles = new ArrayList<>();

    private Context context;
    LayoutInflater myInflater;

    private int layout = R.layout.view_vehicle; // current layout to use

    /**
     * This  is for creating the content for a list of passes listview
     */
    public VehicleArrayAdapter(List<Vehicle> l, Context c) {
        if (!l.isEmpty()) {
            makeVehiclesList(l);
        } else {
            makeVehiclesList(new ArrayList<Vehicle>());
        }
        setContextLayout(c, layout);
    }

    public void setContextLayout(Context c, int layout) {
        context = c;
        this.layout = layout;
    }
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b

    /**
     * This is for creating the content for a list of drivers in listview
     */
    public void makeVehiclesList(List<Vehicle> v) {
        vehicles.add(0, null); // adds empty place holder to position 0
        vehicles.addAll(v);
    }

<<<<<<< HEAD

=======
    private void inflateLayout() {
        myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b
    @Override
    public int getCount() {
        return vehicles.size();
    }

<<<<<<< HEAD
=======
    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b
    @Override
    public Object getItem(int position) {
        return vehicles.get(position);
    }

<<<<<<< HEAD
=======
    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b
    @Override
    public long getItemId(int position) {
        return position;
    }

<<<<<<< HEAD
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = myInflator.inflate(R.layout.view_vehicle, parent, false);
        }
=======
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

        inflateLayout();

        convertView = myInflater.inflate(R.layout.view_vehicle, parent, false);
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b

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
<<<<<<< HEAD
        return convertView;
    }
}
=======

        return convertView;
    }
}
>>>>>>> 2d173e9b3f5ba3d1fc71fc9b10533e01ecb4375b
