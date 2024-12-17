package com.example.myapplication.UI.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.UI.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    // Inserts a vacation into the database PART 1
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    // Updates an existing vacation in the database PART 1
    @Update
    void update(Vacation vacation);

    // Deletes a vacation from the database PART 1
    @Delete
    void delete(Vacation vacation);

    // Retrieves all vacations ordered by their ID
    @Query("SELECT * FROM vacations ORDER BY vacationId ASC")
    LiveData<List<Vacation>> getAllVacations();  // Return LiveData<List<Vacation>>

    // Checks if there are associated excursions for a given vacation ID. Part 1
    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int getExcursionCount(int vacationId);
}
