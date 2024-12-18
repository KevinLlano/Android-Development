package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.UI.database.Repository;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    private EditText editTitle, editHotel, editStartDate, editEndDate;
    private Repository repository;
    private int vacationId = -1;
    private Vacation currentVacation;
    private int numExcursions;

    // RecyclerView and Adapter
    private RecyclerView excursionRecyclerView;
    private ExcursionAdapter excursionAdapter;
    private List<Excursion> excursionList;

    // Part B3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        // Initialize the EditText fields
        editTitle = findViewById(R.id.editTitle);
        editHotel = findViewById(R.id.editHotel);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        // Initialize repository
        repository = new Repository(getApplication());

        // Load vacation details if passed from another activity
        vacationId = getIntent().getIntExtra("vacationId", -1);
        if (vacationId != -1) {
            // Fetch vacation data from the repository
            currentVacation = new Vacation(vacationId,
                    repository.getVacationTitle(vacationId),
                    "", "", "");
            // Set the EditText fields with existing data
            editTitle.setText(currentVacation.getVacationTitle());
            editStartDate.setText(currentVacation.getStartDate());
            editEndDate.setText(currentVacation.getEndDate());
        }

        // RecyclerView setup
        excursionRecyclerView = findViewById(R.id.excursionRecyclerView);
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for excursions
//        excursionList = new ArrayList<>();
//        excursionList.add(new Excursion(1, "Beach Visit", "12-18-2024", vacationId));
//        excursionList.add(new Excursion(2, "Hiking Trip", "12-19-2024", vacationId));

        // Initialize and set the ExcursionAdapter
        excursionAdapter = new ExcursionAdapter(this, excursionList);
        excursionRecyclerView.setAdapter(excursionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Save option
        if (item.getItemId() == R.id.vacationSave) {
            String title = editTitle.getText().toString();
            String hotel = editHotel.getText().toString();
            String startDate = editStartDate.getText().toString();
            String endDate = editEndDate.getText().toString();

            // Validate input dates
            if (!isDateValid(startDate, endDate)) {
                Toast.makeText(this, "End date must be after start date and dates must be in format MM-dd-yyyy", Toast.LENGTH_LONG).show();
                return true;
            }

            // Create a new Vacation object with input data
            Vacation vacation = new Vacation(vacationId, title, hotel, startDate, endDate);

            // Insert or update vacation depending on whether it's new or existing
            if (vacationId == -1) {
                repository.insertVacation(vacation);
            } else {
                repository.updateVacation(vacation);
            }

            // Set alerts for start and end date
            setAlerts(vacation);

            finish();
            return true;
        }

        // Handle Delete option
        if (item.getItemId() == R.id.vacationDelete) {
            numExcursions = repository.getExcursionCount(vacationId);
            if (numExcursions == 0) {
                repository.deleteVacation(currentVacation);
                Toast.makeText(this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Cannot delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        }

        // Handle Share option
        if (item.getItemId() == R.id.vacationShare) {
            shareVacationDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isDateValid(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return end != null && start != null && end.after(start);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setAlerts(Vacation vacation) {
        setAlert(vacation.getStartDate(), "Vacation Starting: " + vacation.getVacationTitle());
        setAlert(vacation.getEndDate(), "Vacation Ending: " + vacation.getVacationTitle());
    }

    private void setAlert(String date, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        try {
            Date alertDate = sdf.parse(date);
            if (alertDate != null) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("message", message);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void shareVacationDetails() {
        String vacationDetails = "Vacation: " + editTitle.getText().toString() + "\n" +
                "Hotel: " + editHotel.getText().toString() + "\n" +
                "Start Date: " + editStartDate.getText().toString() + "\n" +
                "End Date: " + editEndDate.getText().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Vacation Details"));
    }
}
