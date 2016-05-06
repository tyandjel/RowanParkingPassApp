package com.example.android.rowanparkingpass.personinfo;

/**
 * Enum of all states in alphabetical order of state abbreviation
 */
public enum States {
    AK("Alaska"),
    AL("Alabama"),
    AR("Arkansas"),
    AZ("Arizona"),
    CA("California"),
    CO("Colorado"),
    CT("Connecticut"),
    DE("Delaware"),
    FL("Florida"),
    GA("Georgia"),
    HI("Hawaii"),
    IA("Iowa"),
    ID("Idaho"),
    IL("Illinois"),
    IN("Indiana"),
    KS("Kansas"),
    KY("Kentucky"),
    LA("Louisiana"),
    MA("Massachusetts"),
    MD("Maryland"),
    ME("Maine"),
    MI("Michigan"),
    MN("Minnesota"),
    MO("Missouri"),
    MS("Mississippi"),
    MT("Montana"),
    NC("North Carolina"),
    ND("North Dakota"),
    NE("Nebraska"),
    NH("New Hampshire"),
    NJ("New Jersey"),
    NM("New Mexico"),
    NV("Nevada"),
    NY("New York"),
    OH("Ohio"),
    OK("Oklahoma"),
    OR("Oregon"),
    PA("Pennsylvania"),
    RI("Rhode Island"),
    SC("South Carolina"),
    SD("South Dakota"),
    TN("Tennessee"),
    TX("Texas"),
    UT("Utah"),
    VA("Virginia"),
    VT("Vermont"),
    WA("Washington"),
    WI("Wisconsin"),
    WV("West Virginia"),
    WY("Wyoming");


    private String state;

    States(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }

    /**
     * give the position of the state name or -1 if not included.
     *
     * @param value State Name
     * @return position of state
     */
    public static int getPosition(String value) {
        States[] v = values();
        boolean found = false;
        int pos = -1;
        for (int i = 0; i < v.length && !found; i++) {
            String s = v[i].state;
            if ((s).equals(value)) {
                found = true;
                pos = i;
            }
        }
        return pos;
    }

}
