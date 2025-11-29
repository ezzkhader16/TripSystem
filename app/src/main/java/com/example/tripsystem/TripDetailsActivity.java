package com.example.tripsystem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDate, tvType, tvBasePrice, tvTotalPrice, tvDescription;
    private CheckBox cbBreakfast, cbBike;
    private Button btnBack;
    private FloatingActionButton fabEdit;
    private ImageView imgDetail;

    private double basePrice = 0;
    private int index = -1;
    private int cardImageResId = 0;
    private int detailImageResId = 0;

    private static final double BREAKFAST_PRICE = 5.0;
    private static final double BIKE_PRICE = 10.0;
    private static final String TAG = "DetailsLifecycle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_trip_details);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDate = findViewById(R.id.tvDetailDate);
        tvType = findViewById(R.id.tvDetailType);
        tvBasePrice = findViewById(R.id.tvBasePrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvDescription = findViewById(R.id.tvDescription);
        cbBreakfast = findViewById(R.id.cbBreakfast);
        cbBike = findViewById(R.id.cbBike);
        btnBack = findViewById(R.id.btnBack);
        fabEdit = findViewById(R.id.fabEdit);
        imgDetail = findViewById(R.id.imgDetail);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        basePrice = intent.getDoubleExtra("budget", 0);
        String type = intent.getStringExtra("type");
        boolean important = intent.getBooleanExtra("important", false);
        boolean paid = intent.getBooleanExtra("paid", false);
        cardImageResId = intent.getIntExtra("cardImageResId", 0);
        detailImageResId = intent.getIntExtra("detailImageResId", 0);

        tvTitle.setText(title);
        tvDate.setText("Date: " + date);
        tvType.setText("Typ: " + type);
        tvBasePrice.setText("السعر الأساسي: " + basePrice + " JD");

        if (detailImageResId != 0) {
            imgDetail.setImageResource(detailImageResId);
        }

        String desc;

        // based on photo
        if (detailImageResId == R.drawable.jer2) {
            desc = "Explore the Old City of Jerusalem. Walk through the historic markets, "
                    + "ancient alleys, and visit famous religious landmarks like the Dome of the Rock "
                    + "and the Church of the Holy Sepulchre. Free time for photos and trying local street food.";
        } else if (detailImageResId == R.drawable.south2) {
            desc = "Full-day trip to South Lebanon along the Mediterranean coast. Enjoy a scenic drive, "
                    + "sea views, and stops in small coastal towns. Optional swimming time and a café break by the sea.";
        } else if (detailImageResId == R.drawable.rawshe2) {
            desc = "Evening visit to Beirut and the famous Raouche (Pigeon Rocks). Walk along the Corniche, "
                    + "watch the sunset over the cliffs, and enjoy coffee or dinner in one of the nearby cafés.";
        } else {
            // Custom trip
            desc = "Custom trip. Enjoy your time and feel free to add more details about this place.";
        }

        tvDescription.setText(desc);


        updateTotalPrice();

        cbBreakfast.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        cbBike.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());

        btnBack.setOnClickListener(v -> finish());

        fabEdit.setOnClickListener(v -> openEditScreen(title, date, type, important, paid));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    private void updateTotalPrice() {
        double total = basePrice;
        if (cbBreakfast.isChecked()) total += BREAKFAST_PRICE;
        if (cbBike.isChecked()) total += BIKE_PRICE;
        tvTotalPrice.setText("السعر الإجمالي مع الإضافات: " + total + " JD");
    }

    private void openEditScreen(String title, String date, String type, boolean important, boolean paid) {
        if (index == -1) {
            Toast.makeText(this, "Error in the trip", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(TripDetailsActivity.this, AddEditTaskActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("index", index);
        intent.putExtra("title", title);
        intent.putExtra("date", date);
        intent.putExtra("budget", basePrice);
        intent.putExtra("type", type);
        intent.putExtra("important", important);
        intent.putExtra("paid", paid);
        intent.putExtra("cardImageResId", cardImageResId);
        intent.putExtra("detailImageResId", detailImageResId);

        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
