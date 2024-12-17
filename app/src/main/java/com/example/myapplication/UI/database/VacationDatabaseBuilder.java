package com.example.myapplication.UI.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.UI.dao.ExcursionDAO;
import com.example.myapplication.UI.dao.VacationDAO;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.entities.Vacation;

// Define the database class for Room
@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    // Abstract methods to provide DAOs (Data Access Objects)
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    // Singleton instance of the database to ensure only one instance exists
    private static volatile VacationDatabaseBuilder INSTANCE;

    // Method to get the singleton database instance
    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {  // Double-check to avoid race conditions
                    // Build the database with Room's builder
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()  // Wipes and rebuilds the database on version mismatch
                            .build();  // Finalize the database creation
                }
            }
        }
        return INSTANCE;
    }
}
