package com.viamhealth.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by naren on 16/10/13.
 */
public class JsonGraphDataBuilder {

    private JSONObject object = new JSONObject();
    private Integer max, min;

    public JsonGraphDataBuilder() {

    }

    public JsonGraphDataBuilder write(String key, JsonOutput item, JsonOutput.GraphSeries series) {

        try {
            object.put(key, item.toJSON(series));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }

    public JsonGraphDataBuilder writeYAxisExtras(JsonOutput item) {
        JSONObject yaxis = new JSONObject();

        try {
            yaxis.put("max", item.getMax());
            yaxis.put("min", item.getMin());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            object.put("yAxis", yaxis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }

    public JsonGraphDataBuilder write(String key, List readings, JsonOutput.GraphSeries series) {

        JSONArray array = new JSONArray();

        if (readings == null)
            return this;

        for (int i = 0; i < readings.size(); i++) {
            try {
                JsonOutput item = (JsonOutput) readings.get(i);
                array.put(i, item.toJSON(series));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            object.put(key, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String toString() {
        try {
            return object.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface JsonOutput {
        public JSONObject toJSON(GraphSeries series);

        ;

        public int getMax();

        public int getMin();

        public enum GraphSeries {A, B, C, D}

    }


}
