package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.entities.Vacation;
import com.example.myapplication.UI.viewmodel.VacationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private VacationViewModel vacationViewModel;
    private VacationAdapter vacationAdapter;
    private ExcursionAdapter excursionAdapter;
    private RecyclerView recyclerViewExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Initialize ViewModel
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);

        // Initialize RecyclerView and adapter for vacations
        RecyclerView recyclerViewVacations = findViewById(R.id.recyclerviewVacations);
        recyclerViewVacations.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter = new VacationAdapter(this, null);
        recyclerViewVacations.setAdapter(vacationAdapter);

        // Observe the vacations
        vacationViewModel.getAllVacations().observe(this, vacations -> {
            // Update the adapter with the new list of vacations
            if (vacations != null) {
                vacationAdapter.setVacations(vacations);
                vacationAdapter.notifyDataSetChanged();
            } else {
                Log.d("VacationList", "No vacations available to display.");
            }
        });

        // Initialize RecyclerView and adapter for excursions
        recyclerViewExcursions = findViewById(R.id.recyclerviewExcursions);
        recyclerViewExcursions.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter = new ExcursionAdapter(this, null);
        recyclerViewExcursions.setAdapter(excursionAdapter);

        // Observe the excursions
        vacationViewModel.getAllExcursions().observe(this, excursions -> {
            if (excursions != null) {

                int currentVacationId = getCurrentVacationId();
                List<Excursion> currentExcursions = filterExcursionsByVacationId(excursions, currentVacationId);
                excursionAdapter.setExcursions(currentExcursions);
                excursionAdapter.notifyDataSetChanged();
            } else {
                Log.d("VacationList", "No excursions available to display.");
            }
        });

        // Floating Action Button to add new vacations
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    private int getCurrentVacationId() {

        return 1;
    }

    private List<Excursion> filterExcursionsByVacationId(List<Excursion> excursions, int vacationId) {


        return excursions.stream()
                .filter(excursion -> excursion.getVacationID() == vacationId)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.mySample) {
            // Manually add sample vacations and excursions to the database
            Vacation vacation1 = new Vacation(0, "Canada", "Marriott", "08/03/23", "08/09/23");
            Vacation vacation2 = new Vacation(0, "Spain", "Hilton", "09/02/23", "09/14/23");
            vacationViewModel.insertVacation(vacation1);
            vacationViewModel.insertVacation(vacation2);

            // Manually trigger the observation of vacations and excursions again
            vacationViewModel.getAllVacations().observe(this, vacations -> {
                if (vacations != null) {
                    vacationAdapter.setVacations(vacations);
                    vacationAdapter.notifyDataSetChanged();
                }
            });

            // Insert excursions after vacations are added
            vacationViewModel.insertExcursion(new Excursion(0, "Running", "09/03/23", vacation1.getVacationId()));
            vacationViewModel.insertExcursion(new Excursion(0, "Swimming", "09/07/23", vacation2.getVacationId()));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
