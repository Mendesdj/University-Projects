package pt.unl.fct.di.www.canicookit;

import java.util.Arrays;

public enum UnitMeasures {
    G, ml, Lt, Kg, UND;

    UnitMeasures() {
    }

    public static UnitMeasures fromValue(String value) {
        return valueOf(value);
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString ( e.getEnumConstants () ).replaceAll ( "^.|.$", "" ).split ( ", " );
    }


    @Override
    public String toString() {
        String ret = name ();
        return ret.substring(0, 1).toUpperCase() + ret.substring(1);
    }
}