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

            JSONObject root = new JSONObject(json);
            JSONObject name = root.getJSONObject("name");
            String mainName = name.getString("mainName");

            JSONArray alsoKnownAsJsonArray = name.getJSONArray("alsoKnownAs");
            ArrayList<String> alsoKnownAs = jsonArrayToArrayList(alsoKnownAsJsonArray);

            String placeOfOrigin = root.getString("placeOfOrigin");
            String description = root.getString("description");
            String image = root.getString("image");

            JSONArray ingredientsJsonArray = root.getJSONArray("ingredients");
            ArrayList<String> ingredients = jsonArrayToArrayList(ingredientsJsonArray);

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {

            Log.e(LOG_TAG, "JSON parse error.", e);

        }

        return null;

    }

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
