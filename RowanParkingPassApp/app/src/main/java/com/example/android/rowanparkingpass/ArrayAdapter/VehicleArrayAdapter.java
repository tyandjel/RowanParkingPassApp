package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 3/6/2016.
 */
public class VehicleArrayAdapter extends BaseAdapter implements Filterable {

    private List<Vehicle> vehicleList = new ArrayList<>();
    private ArrayList<Vehicle> filteredVehicleList = new ArrayList<>();
    private VehicleFilter vehicleFilter;

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
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    /**
     * This is for creating the content for a list of drivers in listview
     */
    public void makeVehiclesList(List<Vehicle> v) {
        vehicleList.add(0, null); // adds empty place holder to position 0
        vehicleList.addAll(v);
        filteredVehicleList.add(0, null);
        filteredVehicleList.addAll(v);
    }

    private void inflateLayout() {
        myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */

    public int getCount() {
        return vehicleList.size();
    }


    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */

    @Override
    public Object getItem(int position) {
        return vehicleList.get(position);
    }


    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
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

        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.view_vehicle, parent, false);
        }

        TextView newVehicle = (TextView) convertView.findViewById(R.id.new_vehicle_text_view);
        TextView carText = (TextView) convertView.findViewById(R.id.car_text_view);
        TextView plateText = (TextView) convertView.findViewById(R.id.plate_text_view);

        if (position == 0 && getItem(0) == null) {
            newVehicle.setText("+ Create New Vehicle");
            carText.setText("");
            plateText.setText("");
        } else {
            newVehicle.setText("");
            Vehicle cVehicle = vehicleList.get(position);
            carText.setText(cVehicle.getYear() + " " + cVehicle.getMake() + " " + cVehicle.getModel() + " " + cVehicle.getColor());
            plateText.setText(cVehicle.getLicensePlate());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (vehicleFilter == null) {
            vehicleFilter = new VehicleFilter();
        }
        return vehicleFilter;
    }

    private class VehicleFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Vehicle> tempList = new ArrayList<Vehicle>();

                // search content in friend list
                for (Vehicle vehicle : vehicleList) {
                    if (vehicle != null) {
                        if (vehicle.getCarInfo().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            tempList.add(vehicle);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = vehicleList.size();
                filterResults.values = vehicleList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredVehicleList = (ArrayList<Vehicle>) results.values;
            notifyDataSetChanged();
        }
    }
}



