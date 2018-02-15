package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private Sandwich mSandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();

        setTitle(mSandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        ImageView imageIv = findViewById(R.id.image_iv);
        TextView originTv = findViewById(R.id.origin_tv);
        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        TextView alsoKnownTv = findViewById(R.id.also_known_tv);

        Picasso.with(this)
                .load(mSandwich.getImage())
                .into(imageIv);

        originTv.setText(mSandwich.getPlaceOfOrigin());
        descriptionTv.setText(mSandwich.getDescription());

        List<String> ingredients = mSandwich.getIngredients();

        if (ingredients != null && ingredients.size() != 0) {

            int length = ingredients.size();

            for (int i = 0; i < length; i++) {
                ingredientsTv.append(ingredients.get(i));

                if (i != length - 1) {
                    ingredientsTv.append("\n");
                }
            }

        }

        List<String> alsoKnownList = mSandwich.getAlsoKnownAs();

        if (alsoKnownList != null && alsoKnownList.size() != 0) {

            int length = alsoKnownList.size();

            for (int i = 0; i < length; i++) {
                alsoKnownTv.append(alsoKnownList.get(i));

                if (i != length - 1) {
                    alsoKnownTv.append(", ");
                }
            }

            alsoKnownTv.append(".");

        }

    }
}
