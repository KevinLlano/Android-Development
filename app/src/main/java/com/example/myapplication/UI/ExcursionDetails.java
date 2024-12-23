package com.example.myapplication.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.viewmodel.VacationViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    private EditText editTitle, editDate;
    private VacationViewModel vacationViewModel;
    private int excursionId = -1;
    private Excursion currentExcursion;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Initialize fields
        editTitle = findViewById(R.id.editTitle);
        editDate = findViewById(R.id.editDate);

        // Initialize ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        excursionId = getIntent().getIntExtra("excursionId", -1);
        if (excursionId != -1) {
            // Observe the excursion using LiveData and update UI when data changes
            vacationViewModel.getExcursionByID(excursionId, excursion -> {
                currentExcursion = excursion;
                if (currentExcursion != null) {
                    editTitle.setText(currentExcursion.getExcursionTitle());
                    editDate.setText(currentExcursion.getExcursionDate());
                }
            });
        }

        // Set up date picker
        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Log vacation dates for debugging
        Log.d("VacationDates", "Start: " + getIntent().getStringExtra("vacationStartDate") +
                " End: " + getIntent().getStringExtra("vacationEndDate"));
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            String selectedDate = sdf.format(calendar.getTime());
            Log.d("DatePicker", "Selected Date: " + selectedDate); // Log the selected date for testing
            editDate.setText(selectedDate);
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.excursionSave) {
            saveExcursion();
            return true;
        } else if (id == R.id.excursionDelete) {
            deleteExcursion();
            return true;
        } else if (id == R.id.excursionAlert) {
            setAlert();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void saveExcursion() {
        String title = editTitle.getText().toString();
        String date = editDate.getText().toString();
        int vacationId = getIntent().getIntExtra("vacationId", -1);

        // Retrieve vacation start and end dates
        String vacationStartDate = getIntent().getStringExtra("vacationStartDate");
        String vacationEndDate = getIntent().getStringExtra("vacationEndDate");

        Log.d("SaveExcursion", "Title: " + title + " Date: " + date +
                " VacationStart: " + vacationStartDate + " VacationEnd: " + vacationEndDate);

        if (!title.isEmpty() && !date.isEmpty() && vacationId != -1) {
            if (isValidDate(date)) {
                if (isDateWithinVacation(date, vacationStartDate, vacationEndDate)) {
                    if (currentExcursion == null) {
                        currentExcursion = new Excursion(0, title, date, vacationId);
                        vacationViewModel.insertExcursion(currentExcursion);
                    } else {
                        currentExcursion.setExcursionTitle(title);
                        currentExcursion.setExcursionDate(date);
                        vacationViewModel.updateExcursion(currentExcursion);
                    }

                    Toast.makeText(this, "Excursion saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Excursion date must be within the vacation period.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid date format. Please use MM/dd/yy.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Date format validation
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Date range validation
    private boolean isDateWithinVacation(String date, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        sdf.setLenient(false);  // This ensures strict date parsing

        try {
            long excursionDate = sdf.parse(date).getTime();
            long vacationStart = sdf.parse(startDate).getTime();
            long vacationEnd = sdf.parse(endDate).getTime();

            // Debugging: Log the dates
            Log.d("DateValidation", "Excursion Date: " + excursionDate);
            Log.d("DateValidation", "Vacation Start Date: " + vacationStart);
            Log.d("DateValidation", "Vacation End Date: " + vacationEnd);

            return excursionDate >= vacationStart && excursionDate <= vacationEnd;
        } catch (Exception e) {
            Log.e("DateValidation", "Invalid date(s) provided: " + e.getMessage());
            return false; // Invalid dates
        }
    }

    private void deleteExcursion() {
        if (currentExcursion != null) {
            vacationViewModel.deleteExcursion(currentExcursion);
            Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setAlert() {
        if (currentExcursion != null) {
            // Retrieve the excursion title and date
            String excursionTitle = currentExcursion.getExcursionTitle();
            String excursionDate = currentExcursion.getExcursionDate();

            // Show a message with the excursion title and date
            Toast.makeText(this, "Alert set for: " + excursionTitle + " on " + excursionDate, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No excursion details available to set an alert.", Toast.LENGTH_SHORT).show();
        }
    }
}
