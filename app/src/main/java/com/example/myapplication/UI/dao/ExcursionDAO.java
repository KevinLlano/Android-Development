package com.example.myapplication.UI.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.UI.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {

    // Inserts an excursion into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    // Updates an existing excursion in the database
    @Update
    void update(Excursion excursion);

    // Deletes an excursion from the database
    @Delete
    void delete(Excursion excursion);

    // Retrieves all excursions ordered by their ID
    @Query("SELECT * FROM excursions ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    // Retrieves a specific excursion by its ID
    @Query("SELECT * FROM excursions WHERE excursionID = :excursionId LIMIT 1")
    Excursion getExcursionByID(int excursionId);

    // Retrieves excursions by vacation ID
    @Query("SELECT * FROM excursions WHERE vacationID = :vacationId")
    List<Excursion> getExcursionsByVacationId(int vacationId);
}
