package com.errorerrorerror.esplightcontrol.utils;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String JSONArrayfromIntArray(List<Integer> values) {
        JSONArray jsonArray = new JSONArray();
        for (int value : values) {
            jsonArray.put(value);
        }
        return jsonArray.toString();
    }

    @TypeConverter
    public static List<Integer> JSONArrayToIntArray(String values) {
        try {
            JSONArray jsonArray = new JSONArray(values);
            List<Integer> intArray = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                intArray.add(Integer.parseInt(jsonArray.getString(i)));
            }

            return intArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
