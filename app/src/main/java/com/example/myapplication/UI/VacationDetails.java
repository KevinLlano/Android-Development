package com.example.myapplication.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.entities.Vacation;
import com.example.myapplication.UI.viewmodel.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationDetails extends AppCompatActivity {

    private EditText editTitle, editHotel;
    private Button editStartDate, editEndDate;
    private VacationViewModel vacationViewModel;
    private int vacationId = -1;
    private Vacation currentVacation;
    private Calendar calendar = Calendar.getInstance();

    private RecyclerView excursionRecyclerView;
    private ExcursionAdapter excursionAdapter;
    private List<Excursion> excursionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize fields
        editTitle = findViewById(R.id.editTitle);
        editHotel = findViewById(R.id.editHotel);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        // Initialize ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Get vacation details passed from previous activity
        vacationId = getIntent().getIntExtra("vacationId", -1);
        if (vacationId != -1) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                currentVacation = vacationViewModel.getVacationById(vacationId);
                runOnUiThread(() -> {
                    if (currentVacation != null) {
                        editTitle.setText(currentVacation.getVacationTitle());
                        editHotel.setText(currentVacation.getVacationHotel());
                        editStartDate.setText(currentVacation.getStartDate());
                        editEndDate.setText(currentVacation.getEndDate());
                    }
                });

                // Fetch excursions asynchronously using the ViewModel method
                vacationViewModel.getExcursionsByVacationId(vacationId, excursions -> runOnUiThread(() -> updateUIWithExcursions(excursions)));
            });
        }

        // Set up RecyclerView for excursions
        excursionList = new ArrayList<>();
        excursionRecyclerView = findViewById(R.id.excursionRecyclerView);
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter = new ExcursionAdapter(this, excursionList);
        excursionRecyclerView.setAdapter(excursionAdapter);

        // Set up date pickers
        editStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        editEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        // Initialize FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationId", vacationId);
            intent.putExtra("vacationStartDate", editStartDate.getText().toString());
            intent.putExtra("vacationEndDate", editEndDate.getText().toString());
            startActivity(intent);
        });
    }

    private void showDatePickerDialog(boolean isStartDate) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            String selectedDate = sdf.format(calendar.getTime());

            if (isStartDate) {
                editStartDate.setText(selectedDate);
            } else {
                editEndDate.setText(selectedDate);
            }
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        // Handle save action
        if (item.getItemId() == R.id.vacationSave) {
            saveVacation();
            return true;
        }

        // Handle delete action
        if (item.getItemId() == R.id.vacationDelete) {
            deleteVacation();
            return true;
        }

        // Handle alert action
        if (item.getItemId() == R.id.alertStart) {
            setAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveVacation() {
        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();

        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            Toast.makeText(this, "Invalid date format. Please use MM/dd/yy.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty() || hotel.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create or update Vacation entity
        if (vacationId == -1) {
            Vacation vacation = new Vacation(0, title, hotel, startDate, endDate);
            vacationViewModel.insertVacation(vacation);
        } else {
            currentVacation.setVacationTitle(title);
            currentVacation.setVacationHotel(hotel);
            currentVacation.setStartDate(startDate);
            currentVacation.setEndDate(endDate);
            vacationViewModel.updateVacation(currentVacation);
        }

        // Perform database operations in a background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> runOnUiThread(() -> {
            Toast.makeText(this, "Vacation saved", Toast.LENGTH_SHORT).show();
            finish();
        }));
    }

    private void updateUIWithExcursions(List<Excursion> excursions) {
        excursionAdapter.setExcursions(excursions);
        excursionAdapter.notifyDataSetChanged();
    }

    private void deleteVacation() {
        if (currentVacation != null) {
            vacationViewModel.deleteVacation(currentVacation);
            Toast.makeText(this, "Vacation deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No vacation to delete", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to set alerts for start and end dates
    private void setAlert() {
        if (currentVacation != null) {
            String vacationTitle = currentVacation.getVacationTitle();
            String startDate = currentVacation.getStartDate();
            String endDate = currentVacation.getEndDate();

            // Simulate alert for start date
            Toast.makeText(this, "Alert set for vacation start: " + vacationTitle + " starting on " + startDate, Toast.LENGTH_LONG).show();

            // Simulate alert for end date
            Toast.makeText(this, "Alert set for vacation end: " + vacationTitle + " ending on " + endDate, Toast.LENGTH_LONG).show();
        }
    }
}
