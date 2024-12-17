package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//import com.example.myapplication.UI.entities.Excursion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.myapplication.R;
import com.example.myapplication.UI.database.Repository;
import com.example.myapplication.UI.entities.Vacation;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository; // Repository instance to manage data operations
    private VacationAdapter vacationAdapter; // Adapter for the RecyclerView to display vacations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Initialize repository and adapter
        repository = new Repository(getApplication());
        vacationAdapter = new VacationAdapter(this);

        // Setup RecyclerView
        setupRecyclerView();

        // Floating Action Button to add new vacations
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    // Method to initialize RecyclerView
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(vacationAdapter); // Set adapter to RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateVacationList();
    }

    // Method to update the vacation list from the repository
    private void updateVacationList() {
        List<Vacation> allVacations = repository.getAllVacations().getValue();
        vacationAdapter.setVacations(allVacations); // Pass vacation list to the adapter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the options menu for menu_vacation_list.xml
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.mysample) {
            // Insert sample vacation data
            Vacation vacation = new Vacation(0, "Ecuador", "Oro Verde Hotel", "11-21-2024", "04-12-2024");
            repository.insertVacation(vacation);
            vacation = new Vacation(0, "Japan", "Wyndham Hotel", "07-10-2024", "07-22-2024");
            repository.insertVacation(vacation);

            // Insert sample excursions (commented out for now)
//            Excursion excursion = new Excursion(0, "Racing", "09/03/23");
//            repository.insertExcursion(excursion);
//            excursion = new Excursion(0, "Swimming", "09/03/23");
//            repository.insertExcursion(excursion);

            updateVacationList();
            return true;
        }

        return super.onOptionsItemSelected(item); // Default menu handling
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateVacationList(); // Refresh vacation list when returning to this activity
    }
}
