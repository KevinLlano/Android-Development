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
    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    // Retrieves excursions associated with a specific vacation ID
    @Query("SELECT * FROM EXCURSIONS WHERE vacationID = :vacation ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacation);
}
