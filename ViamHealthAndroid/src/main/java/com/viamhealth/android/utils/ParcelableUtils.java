package com.viamhealth.android.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 22/11/13.
 */
public class ParcelableUtils {

////////////////////////////////////////////////////////////////////
// Parcel methods

    /**
     * Reads a Map from a Parcel that was stored using a String array and a Bundle.
     *
     * @param in   the Parcel to retrieve the map from
     * @param type the class used for the value objects in the map, equivalent to V.class before type erasure
     * @return a map containing the items retrieved from the parcel
     */
    public static <V extends Parcelable> Map<Integer, V> readMap(Parcel in, Class<? extends V> type) {

        Map<Integer, V> map = new HashMap<Integer, V>();
        if (in != null) {
            //String[] keys = in.createStringArray();
            int[] keys = in.createIntArray();
            Bundle bundle = in.readBundle(type.getClassLoader());
            for (Integer key : keys)
                map.put(key, type.cast(bundle.getParcelable(key.toString())));
        }
        return map;
    }


    /**
     * Reads into an existing Map from a Parcel that was stored using a String array and a Bundle.
     *
     * @param map  the Map<String,V> that will receive the items from the parcel
     * @param in   the Parcel to retrieve the map from
     * @param type the class used for the value objects in the map, equivalent to V.class before type erasure
     */
    public static <V extends Parcelable> void readMap(Map<Integer, V> map, Parcel in, Class<V> type) {

        if (map != null) {
            map.clear();
            if (in != null) {
                int[] keys = in.createIntArray();
                Bundle bundle = in.readBundle(type.getClassLoader());
                for (Integer key : keys)
                    map.put(key, type.cast(bundle.getParcelable(key.toString())));
            }
        }
    }


    /**
     * Writes a Map to a Parcel using a String array and a Bundle.
     *
     * @param map the Map<String,V> to store in the parcel
     * @param in  the Parcel to store the map in
     */
    public static void writeMap(Map<Integer, ? extends Parcelable> map, Parcel out) {

        if (map != null && map.size() > 0) {
            Set<Integer> keySet = map.keySet();
            Bundle b = new Bundle();
            for (Integer key : keySet)
                b.putParcelable(key.toString(), map.get(key));
            Integer[] array = keySet.toArray(new Integer[keySet.size()]);
            out.writeIntArray(convert(array));
            out.writeBundle(b);
        } else {
            //String[] array = Collections.<String>emptySet().toArray(new String[0]);
            // you can use a static instance of String[0] here instead
            out.writeIntArray(new int[0]);
            out.writeBundle(Bundle.EMPTY);
        }
    }

    private static int[] convert(Integer[] array) {
        int[] ret = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].intValue();
        }
        return ret;
    }
}
