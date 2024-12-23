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

    // Updates an existing vacation
    @Update
    void update(Vacation vacation);

    // Deletes a vacation
    @Delete
    void delete(Vacation vacation);



    // Retrieves all vacations ordered by ID
    @Query("SELECT * FROM vacations ORDER BY vacationId ASC")
    List<Vacation> getAllVacations();

    // Retrieves a single vacation by ID
    @Query("SELECT * FROM vacations WHERE vacationId = :vacationId LIMIT 1")
    Vacation getVacationByID(int vacationId);


}
