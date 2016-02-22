package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

/**
 * Created by John Saunders on 2/21/2016.
 * I extended Serializable because I could not impliment twice in the other classes.
 * This class allows us to have one List input in the ListViewArrayAdapter constructor
 * Because the all share is interface.
 */
public interface PersonInfo extends Serializable {

}
