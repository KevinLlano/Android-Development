package com.example.myapplication.UI.dao;

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

    // Inserts a vacation into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    // Updates an existing vacation in the database
    @Update
    void update(Vacation vacation);

    // Deletes a vacation from the database
    @Delete
    void delete(Vacation vacation);

    // Retrieves all vacations ordered by their ID
    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();
}
