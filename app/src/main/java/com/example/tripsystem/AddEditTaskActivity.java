package com.example.tripsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {
    private static final String TAG = "AddEditLifecycle";
    private EditText etTitle, etBudget;
    private TextView tvDateValue, tvFormTitle;
    private ImageButton btnPickDate;
    private RadioGroup rgType;
    private RadioButton rbTypeTour, rbTypeFood, rbTypeShopping;
    private CheckBox cbImportant;
    private Switch swPaid;
    private Button btnSave;
    private Spinner spImage;

    private int selectedYear, selectedMonth, selectedDay;
    private int editingIndex = -1;

    private int selectedCardImageResId = 0;
    private int selectedDetailImageResId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_add_edit_task);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etTitle = findViewById(R.id.etTitle);
        tvDateValue = findViewById(R.id.tvDateValue);
        btnPickDate = findViewById(R.id.btnPickDate);
        etBudget = findViewById(R.id.etBudget);
        rgType = findViewById(R.id.rgType);
        rbTypeTour = findViewById(R.id.rbTypeTour);
        rbTypeFood = findViewById(R.id.rbTypeFood);
        rbTypeShopping = findViewById(R.id.rbTypeShopping);
        cbImportant = findViewById(R.id.cbImportant);
        swPaid = findViewById(R.id.swPaid);
        btnSave = findViewById(R.id.btnSave);
        spImage = findViewById(R.id.spImage);

        // Spinner لاختيار المكان
        String[] imageOptions = {"اختر المكان", "Jer", "Rawshe", "South"};
        ArrayAdapter<String> imgAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                imageOptions
        );
        imgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImage.setAdapter(imgAdapter);

        spImage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                switch (position) {
                    case 1: // jer
                        selectedCardImageResId = R.drawable.jer;
                        selectedDetailImageResId = R.drawable.jer2;
                        break;
                    case 2: // rawshe
                        selectedCardImageResId = R.drawable.rawshe;
                        selectedDetailImageResId = R.drawable.rawshe2;
                        break;
                    case 3: // south
                        selectedCardImageResId = R.drawable.south;
                        selectedDetailImageResId = R.drawable.south2;
                        break;
                    default:
                        selectedCardImageResId = 0;
                        selectedDetailImageResId = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // DatePicker
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    AddEditTaskActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;
                        String dateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvDateValue.setText(dateStr);
                    },
                    selectedYear, selectedMonth, selectedDay
            );
            dialog.show();
        });

        // are we in edit mode?
        String mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            tvFormTitle.setText("Edit trip");

            editingIndex = getIntent().getIntExtra("index", -1);

            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            double budget = getIntent().getDoubleExtra("budget", 0);
            String type = getIntent().getStringExtra("type");
            boolean important = getIntent().getBooleanExtra("important", false);
            boolean paid = getIntent().getBooleanExtra("paid", false);
            int cardRes = getIntent().getIntExtra("cardImageResId", 0);
            int detailRes = getIntent().getIntExtra("detailImageResId", 0);

            selectedCardImageResId = cardRes;
            selectedDetailImageResId = detailRes;

            etTitle.setText(title);
            tvDateValue.setText(date);
            etBudget.setText(String.valueOf(budget));
            cbImportant.setChecked(important);
            swPaid.setChecked(paid);

            if ("سياحة".equals(type)) {
                rbTypeTour.setChecked(true);
            } else if ("أكل".equals(type)) {
                rbTypeFood.setChecked(true);
            } else if ("تسوق".equals(type)) {
                rbTypeShopping.setChecked(true);
            }

            // add spinner based on photo
            if (cardRes == R.drawable.jer) {
                spImage.setSelection(1);
            } else if (cardRes == R.drawable.rawshe) {
                spImage.setSelection(2);
            } else if (cardRes == R.drawable.south) {
                spImage.setSelection(3);
            } else {
                spImage.setSelection(0);
            }
        } else {
            tvFormTitle.setText("Add new trip");
        }

        btnSave.setOnClickListener(v -> saveTask());
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

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String date = tvDateValue.getText().toString().trim();
        String budgetStr = etBudget.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Tittle is needed");
            return;
        }
        if (date.equals("Choice date")) {
            Toast.makeText(this, "Please choice the date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (budgetStr.isEmpty()) {
            etBudget.setError("Budget is needed");
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetStr);
        } catch (NumberFormatException e) {
            etBudget.setError("Input valid number");
            return;
        }

        String type = "";
        int selectedId = rgType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbTypeTour) {
            type = "سياحة";
        } else if (selectedId == R.id.rbTypeFood) {
            type = "أكل";
        } else if (selectedId == R.id.rbTypeShopping) {
            type = "تسوق";
        } else {
            Toast.makeText(this, "Please select type of trip", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isImportant = cbImportant.isChecked();
        boolean isPaid = swPaid.isChecked();


        if (selectedCardImageResId == 0 || selectedDetailImageResId == 0) {
            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show();
            return;
        }

        TripTask task = new TripTask(title, date, budget, isImportant, type, isPaid,
                selectedCardImageResId, selectedDetailImageResId);

        Intent result = new Intent();
        result.putExtra("title", task.getTitle());
        result.putExtra("date", task.getDate());
        result.putExtra("budget", task.getBudget());
        result.putExtra("type", task.getType());
        result.putExtra("important", task.isImportant());
        result.putExtra("paid", task.isPaid());
        result.putExtra("cardImageResId", task.getCardImageResId());
        result.putExtra("detailImageResId", task.getDetailImageResId());

        if (editingIndex != -1) {
            result.putExtra("index", editingIndex);
        }

        setResult(RESULT_OK, result);
        finish();
    }
    }


