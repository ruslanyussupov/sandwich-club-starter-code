package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String BUNDLE_IMAGE_PATH = "imagePath";
    private static final String BUNDLE_ORIGIN = "origin";
    private static final String BUNDLE_DESCRIPTION = "description";
    private static final String BUNDLE_INGREDIENTS = "ingredients";
    private static final String BUNDLE_ALSO_KNOWN = "alsoKnown";

    private ImageView mImageIv;
    private TextView mOriginTv;
    private TextView mOriginLabelTv;
    private TextView mDescriptionTv;
    private TextView mDescriptionLabelTv;
    private TextView mIngredientsTv;
    private TextView mIngredientsLabelTv;
    private TextView mAlsoKnownTv;
    private TextView mAlsoKnownLabelTv;

    private String mImagePath;
    private String mOrigin;
    private String mDescription;
    private List<String> mIngredientsList;
    private List<String> mAlsoKnownList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Bind views
        mImageIv = findViewById(R.id.image_iv);
        mOriginTv = findViewById(R.id.origin_tv);
        mOriginLabelTv = findViewById(R.id.origin_label);
        mDescriptionTv = findViewById(R.id.description_tv);
        mDescriptionLabelTv = findViewById(R.id.description_label);
        mIngredientsTv = findViewById(R.id.ingredients_tv);
        mIngredientsLabelTv = findViewById(R.id.ingredients_label);
        mAlsoKnownTv = findViewById(R.id.also_known_tv);
        mAlsoKnownLabelTv = findViewById(R.id.also_known_label);

        // Get data from the JSON if nothing to restore from the Bundle
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }

            int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
            if (position == DEFAULT_POSITION) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }

            String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
            String json = sandwiches[position];
            Sandwich mSandwich = JsonUtils.parseSandwichJson(json);
            if (mSandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            mImagePath = mSandwich.getImage();
            mOrigin = mSandwich.getPlaceOfOrigin();
            mDescription = mSandwich.getDescription();
            mIngredientsList = mSandwich.getIngredients();
            mAlsoKnownList = mSandwich.getAlsoKnownAs();

            populateUI();

            setTitle(mSandwich.getMainName());

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(BUNDLE_IMAGE_PATH, mImagePath);
        outState.putStringArrayList(BUNDLE_ALSO_KNOWN, (ArrayList<String>) mAlsoKnownList);
        outState.putString(BUNDLE_ORIGIN, mOrigin);
        outState.putString(BUNDLE_DESCRIPTION, mDescription);
        outState.putStringArrayList(BUNDLE_INGREDIENTS, (ArrayList<String>) mIngredientsList);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mImagePath = savedInstanceState.getString(BUNDLE_IMAGE_PATH);
        mAlsoKnownList = savedInstanceState.getStringArrayList(BUNDLE_ALSO_KNOWN);
        mOrigin = savedInstanceState.getString(BUNDLE_ORIGIN);
        mDescription = savedInstanceState.getString(BUNDLE_DESCRIPTION);
        mIngredientsList = savedInstanceState.getStringArrayList(BUNDLE_INGREDIENTS);

        populateUI();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    // If data exists populate a View, otherwise hide it
    private void populateUI() {

        if (TextUtils.isEmpty(mImagePath)) {
            mImageIv.setVisibility(View.GONE);
        } else {
            mImageIv.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(mImagePath)
                    .into(mImageIv);
        }

        if (TextUtils.isEmpty(mOrigin)) {
            hideContent(mOriginLabelTv, mOriginTv);
        } else {
            showContent(mOriginLabelTv, mOriginTv);
            mOriginTv.setText(mOrigin);
        }

        if (TextUtils.isEmpty(mDescription)) {
            hideContent(mDescriptionLabelTv, mDescriptionTv);
        } else {
            showContent(mDescriptionLabelTv, mDescriptionTv);
            mDescriptionTv.setText(mDescription);
        }

        if (mIngredientsList == null || mIngredientsList.size() == 0) {
            hideContent(mIngredientsLabelTv, mIngredientsTv);
        } else {
            showContent(mIngredientsLabelTv, mIngredientsTv);

            int length = mIngredientsList.size();

            for (int i = 0; i < length; i++) {
                mIngredientsTv.append(mIngredientsList.get(i));

                // If the ingredient not last, add next line
                if (i != length - 1) {
                    mIngredientsTv.append("\n");
                }
            }
        }

        if (mAlsoKnownList == null || mAlsoKnownList.size() == 0) {
            hideContent(mAlsoKnownLabelTv, mAlsoKnownTv);
        } else {
            showContent(mAlsoKnownLabelTv, mAlsoKnownTv);

            int length = mAlsoKnownList.size();

            for (int i = 0; i < length; i++) {
                mAlsoKnownTv.append(mAlsoKnownList.get(i));

                // If the name not last, add comma
                if (i != length - 1) {
                    mAlsoKnownTv.append(", ");
                }
            }
            // Add dot in the end
            mAlsoKnownTv.append(".");
        }
    }

    private void showContent(View labelView, View bodyView) {
        labelView.setVisibility(View.VISIBLE);
        bodyView.setVisibility(View.VISIBLE);
    }

    private void hideContent(View labelView, View bodyView) {
        labelView.setVisibility(View.GONE);
        bodyView.setVisibility(View.GONE);
    }

}
