package com.example.android.rowanparkingpass.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.rowanparkingpass.Activities.BaseActivity;
import com.example.android.rowanparkingpass.R;
import com.example.android.rowanparkingpass.personinfo.Driver;
import com.example.android.rowanparkingpass.personinfo.Pass;
import com.example.android.rowanparkingpass.personinfo.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnathan Saunders on 2/16/2016.
 * This class will be in charge of handling the content put into the activity_list_view
 * it will fullName activities screens 2,3,4,6 and 7 (home, driver, vehicle pages, View Drivers, and View Vehicles).
 */
public class ListViewArrayAdapter extends BaseAdapter {
    private List<Driver> drivers = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Pass> passes = new ArrayList<>();

    private Context context;
    LayoutInflater myInflater;

    int layout; // current layout to use
    int passesView = R.layout.view_recent_pass;
    int vehiclesView = R.layout.view_vehicle;
    int driversView = R.layout.view_driver;

    /**
     * This  is for creating the content for a list of passes listview
     */
    public ListViewArrayAdapter(List<?> l, Context c, int layout, String mode) {
        if (!l.isEmpty()) {
            Object objIn = l.get(0);
            if (objIn instanceof Pass) {
                makePassesList((List<Pass>) l);
            } else if (objIn instanceof Vehicle) {
                makeVehiclesList((List<Vehicle>) l);
            } else if (objIn instanceof Driver) {
                makeDriversList((List<Driver>) l);
            }

        } else {
            if (mode.equals(BaseActivity.mode.HOME_PAGE.name())) {
                makePassesList(new ArrayList<Pass>());
            } else if (mode.equals(BaseActivity.mode.DRIVERS.name()) ||
                    mode.equals(BaseActivity.mode.DRIVERS_LIST.name())) {
                makeDriversList(new ArrayList<Driver>());
            } else if (mode.equals(BaseActivity.mode.VEHICLES.name()) ||
                    mode.equals(BaseActivity.mode.VEHICLES_LIST.name())) {
                makeVehiclesList(new ArrayList<Vehicle>());
            }
        }
        setContextLayout(c, layout);
    }

    public void setContextLayout(Context c, int layout) {
        context = c;
        this.layout = layout;
    }

    /**
     * This is for creating the content for a list of drivers in listview
     */
    public void makeVehiclesList(List<Vehicle> v) {
        vehicles.add(0, null); // adds empty place holder to position 0
        vehicles.addAll(v);
    }

    /**
     * This is for creating the content for a list of vehicles in listview
     */
    public void makeDriversList(List<Driver> d) {
        drivers.add(0, null); // adds empty place holder to position 0
        drivers.addAll(d);
    }

    /**
     * This is for creating the content for a list of Passes in listview
     */
    public void makePassesList(List<Pass> p) {
        passes.add(0, null); // adds empty place holder to position 0
        passes.addAll(p);
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
        int cnt = 0;
        if (passesView == layout) {
            cnt = passes.size();
        } else if (vehiclesView == layout) {
            cnt = vehicles.size();
        } else if (driversView == layout) {
            cnt = drivers.size();
        }
        return cnt;
    } // to be changed to account for drivers an vehicles use

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        Object item = null;
        if (passesView == layout) {
            item = passes.get(position);
        } else if (vehiclesView == layout) {
            item = vehicles.get(position);
        } else if (driversView == layout) {
            item = drivers.get(position);
        }
        return item;
    } // to be changed to account for drivers an vehicles use

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

        inflateLayout();
        // checks which layout we should be using.
        switch (layout) {
            // Recent pass section
            case R.layout.view_recent_pass:
                convertView = myInflater.inflate(R.layout.view_recent_pass, parent, false);

                TextView newPass = (TextView) convertView.findViewById(R.id.new_pass_text_view);
                TextView driverName = (TextView) convertView.findViewById(R.id.driver_text_view);
                TextView address = (TextView) convertView.findViewById(R.id.address_text_view);
                TextView townCity = (TextView) convertView.findViewById(R.id.town_city_text_view);
                TextView car = (TextView) convertView.findViewById(R.id.car_text_view);
                TextView plate = (TextView) convertView.findViewById(R.id.plate_text_view);
                //String [] lArr = new String[list.size()];
                // lArr=list.toArray(lArr); not needed
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
                break;
            // Recent drivers section
            case R.layout.view_driver:
                convertView = myInflater.inflate(R.layout.view_driver, parent, false);

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
                break;
            // Recent vehicles section
            case R.layout.view_vehicle:
                convertView = myInflater.inflate(R.layout.view_vehicle, parent, false);

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
                break;
            default:
                break;
        }
        return convertView;
    }
}
