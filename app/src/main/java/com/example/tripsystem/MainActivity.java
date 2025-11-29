package com.example.tripsystem;


import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLifecycle";
    private RecyclerView rvTasks;
    private EditText etSearch;
    private FloatingActionButton fabAdd;

    private ArrayList<TripTask> taskList = new ArrayList<>();
    private Adapter adapter;

    private static final String PREFS_NAME = "trip_prefs";
    private static final String KEY_TASKS = "tasks_json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        rvTasks = findViewById(R.id.rvTasks);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        loadTasksFromPrefs();
        if (taskList.isEmpty()) {
            addDefaultTrips();
            saveTasksToPrefs();
        }


        adapter = new Adapter(taskList);
        rvTasks.setAdapter(adapter);


        adapter.setOnItemClickListener(position -> {
            TripTask clicked = taskList.get(position);
            Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);
            intent.putExtra("index", position);
            intent.putExtra("title", clicked.getTitle());
            intent.putExtra("date", clicked.getDate());
            intent.putExtra("budget", clicked.getBudget());
            intent.putExtra("type", clicked.getType());
            intent.putExtra("important", clicked.isImportant());
            intent.putExtra("paid", clicked.isPaid());
            intent.putExtra("cardImageResId", clicked.getCardImageResId());
            intent.putExtra("detailImageResId", clicked.getDetailImageResId());
            startActivityForResult(intent, 100);
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra("mode", "add");
            startActivityForResult(intent, 101);
        });
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


    private void filterList(String text) {
        ArrayList<TripTask> filtered = new ArrayList<>();
        for (TripTask t : taskList) {
            if (t.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(t);
            }
        }
        adapter.updateList(filtered);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        String title = data.getStringExtra("title");
        String date = data.getStringExtra("date");
        double budget = data.getDoubleExtra("budget", 0);
        String type = data.getStringExtra("type");
        boolean important = data.getBooleanExtra("important", false);
        boolean paid = data.getBooleanExtra("paid", false);
        int cardImageResId = data.getIntExtra("cardImageResId", 0);
        int detailImageResId = data.getIntExtra("detailImageResId", 0);
        int index = data.getIntExtra("index", -1);

        TripTask task = new TripTask(title, date, budget, important, type, paid,
                cardImageResId, detailImageResId);

        if (index == -1) {

            taskList.add(task);
            Toast.makeText(this, "Trip added succefuly", Toast.LENGTH_SHORT).show();
        } else {

            if (index >= 0 && index < taskList.size()) {
                taskList.set(index, task);
                Toast.makeText(this, "Trip editd succefuly", Toast.LENGTH_SHORT).show();
            }
        }

        adapter.updateList(new ArrayList<>(taskList));
        saveTasksToPrefs();
    }

    private void saveTasksToPrefs() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (TripTask t : taskList) {
                JSONObject obj = new JSONObject();
                obj.put("title", t.getTitle());
                obj.put("date", t.getDate());
                obj.put("budget", t.getBudget());
                obj.put("type", t.getType());
                obj.put("important", t.isImportant());
                obj.put("paid", t.isPaid());
                obj.put("cardImageResId", t.getCardImageResId());
                obj.put("detailImageResId", t.getDetailImageResId());
                jsonArray.put(obj);
            }
            String jsonString = jsonArray.toString();
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_TASKS, jsonString);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromPrefs() {
        taskList.clear();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String jsonString = prefs.getString(KEY_TASKS, null);

        if (jsonString == null || jsonString.isEmpty()) {
            // add 3 trip dafult
            addDefaultTrips();
            saveTasksToPrefs();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String title = obj.getString("title");
                String date = obj.getString("date");
                double budget = obj.getDouble("budget");
                String type = obj.getString("type");
                boolean important = obj.getBoolean("important");
                boolean paid = obj.getBoolean("paid");
                int cardImageResId = obj.optInt("cardImageResId", 0);
                int detailImageResId = obj.optInt("detailImageResId", 0);

                TripTask t = new TripTask(title, date, budget, important, type, paid,
                        cardImageResId, detailImageResId);
                taskList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDefaultTrips() {
        taskList.clear();

        // 1) Jerusalem
        taskList.add(new TripTask(
                "Jerusalem - Old City",
                "20/12/2025",
                40.0,
                true,
                "سياحة",       // type
                false,
                R.drawable.jer,
                R.drawable.jer2
        ));

        // 2) South Lebanon
        taskList.add(new TripTask(
                "South Lebanon Coastal Trip",
                "25/12/2025",
                55.0,
                false,
                "سياحة",
                false,
                R.drawable.south,
                R.drawable.south2
        ));

        // 3) Beirut - Raouche
        taskList.add(new TripTask(
                "Beirut - Raouche Rocks",
                "30/12/2025",
                60.0,
                false,
                "سياحة",
                false,
                R.drawable.rawshe,
                R.drawable.rawshe2
        ));
    }


}