package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Pass;

import java.util.ArrayList;
import java.util.List;

public class PassArrayAdapter extends ListViewArrayAdapter {

    private List<Pass> passesList = new ArrayList<>();
    private ArrayList<Pass> filteredPassesList = new ArrayList<>();
    private PassFilter passFilter;

    private Context context;
    LayoutInflater myInflater;

    private int layout = R.layout.view_recent_pass; // current layout to use

    /**
     * This  is for creating the content for a list of passesList listview
     */
    public PassArrayAdapter(List<Pass> l, Context c) {
        if (!l.isEmpty()) {
            makePassesList(l);

        } else {
            makePassesList(new ArrayList<Pass>());
        }
        setContextLayout(c, layout);
        getFilter();
    }

    public void setContextLayout(Context c, int layout) {
        context = c;
        this.layout = layout;
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * This is for creating the content for a list of Passes in listview
     */
    public void makePassesList(List<Pass> p) {
        passesList.add(new Pass()); // adds empty place holder to position 0
        passesList.addAll(p);
        filteredPassesList.add(0, null);
        filteredPassesList.addAll(p);
    }


    private void inflateLayout() {
        myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return filteredPassesList.size();
    }


    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return filteredPassesList.get(position);
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

    @Override

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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.view_recent_pass, parent, false);
        }
        TextView newPass = (TextView) convertView.findViewById(R.id.new_pass_text_view);
        TextView driverName = (TextView) convertView.findViewById(R.id.driver_text_view);
        TextView address = (TextView) convertView.findViewById(R.id.address_text_view);
        TextView townCity = (TextView) convertView.findViewById(R.id.town_city_text_view);
        TextView car = (TextView) convertView.findViewById(R.id.car_text_view);
        TextView color = (TextView) convertView.findViewById(R.id.car_color);
        TextView plate = (TextView) convertView.findViewById(R.id.plate_text_view);
        TextView carColorText = (TextView) convertView.findViewById(R.id.car_color_text);
        //String [] lArr = new String[list.size()];
        // lArr=list.toArray(lArr); not needed
        if (position == 0) {
            newPass.setText("+ Create New Pass");
            driverName.setText("");
            address.setText("");
            townCity.setText("");
            car.setText("");
            color.setText("");
            color.setBackgroundColor(0);
            plate.setText("");
            carColorText.setText("");
        } else {
            newPass.setText("");
            Pass cPass = filteredPassesList.get(position);
            driverName.setText(cPass.getDriver().getName());
            address.setText(cPass.getDriver().getStreet());
            townCity.setText(cPass.getDriver().getTown() + " " + cPass.getDriver().getState() + ", " + cPass.getDriver().getZipCode());
            car.setText(cPass.getVehicle().getYear() + " " + cPass.getVehicle().getMake() + " " + cPass.getVehicle().getModel());
            color.setBackgroundColor(Integer.parseInt(cPass.getVehicle().getColor()));
            color.setTextColor(Integer.parseInt(cPass.getVehicle().getColor()));
            plate.setText(cPass.getVehicle().getVehicleState() + " " + cPass.getVehicle().getLicensePlate());
        }
        return animateList(position, convertView);
    }

    @Override
    public Filter getFilter() {
        if (passFilter == null) {
            passFilter = new PassFilter();
        }
        return passFilter;
    }

    private class PassFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Pass> tempList = new ArrayList<>();

                // search content in friend list
                for (Pass pass : passesList) {
                    if (pass != null) {
                        if (pass.getVehicle().getCarInfo().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            tempList.add(pass);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = passesList.size();
                filterResults.values = passesList;
            }

            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredPassesList = (ArrayList<Pass>) results.values;
            notifyDataSetChanged();
        }
    }
}