package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {

        try {
            // Get JSONObject from given JSON String
            JSONObject root = new JSONObject(json);
            JSONObject name = root.getJSONObject("name");

            // Get sandwich's name
            String mainName = name.getString("mainName");

            // Get list of alternative sandwich's names
            JSONArray alsoKnownAsJsonArray = name.getJSONArray("alsoKnownAs");
            ArrayList<String> alsoKnownAs = jsonArrayToArrayList(alsoKnownAsJsonArray);

            // Get sandwich's origin
            String placeOfOrigin = root.getString("placeOfOrigin");

            // Get sandwich's description
            String description = root.getString("description");

            // Get sandwich image string URL
            String image = root.getString("image");

            // Get list of ingredients
            JSONArray ingredientsJsonArray = root.getJSONArray("ingredients");
            ArrayList<String> ingredients = jsonArrayToArrayList(ingredientsJsonArray);

            // Return Sandwich instance contains data we got from the JSON
            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {

            Log.e(LOG_TAG, "JSON parse error.", e);

        }

        // Return null if JSON parse failed
        return null;

    }


    // Takes JSONArray and converts it to ArrayList<String>
    private static ArrayList<String> jsonArrayToArrayList(JSONArray jsonArray) throws JSONException {

        int length = jsonArray.length();

        if (length == 0) {
            return null;
        }

        ArrayList<String> arrayList = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            arrayList.add(jsonArray.getString(i));
        }

        return arrayList;

    }
}
