package com.example.myapplication.UI.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.UI.dao.ExcursionDAO;
import com.example.myapplication.UI.dao.VacationDAO;
import com.example.myapplication.UI.entities.Excursion;
import com.example.myapplication.UI.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    // Retrieve all vacations (LiveData for UI updates, real time)
    public LiveData<List<Vacation>> getAllVacations() {
        return mVacationDAO.getAllVacations();
    }


    // Insert vacation
    public void insertVacation(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.insert(vacation));
    }

    // Update vacation
    public void updateVacation(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    // Delete vacation and perform validation check
    public void deleteVacation(Vacation vacation) {
        databaseExecutor.execute(() -> {
            int excursionCount = mVacationDAO.getExcursionCount(vacation.getVacationId());
            if (excursionCount == 0) {
                mVacationDAO.delete(vacation);
            } else {
                throw new IllegalStateException("Cannot delete vacation with associated excursions.");
            }
        });
    }

    // Insert excursion
    public void insertExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.insert(excursion));
    }

    // Update excursion
    public void updateExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    // Delete excursion
    public void deleteExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.delete(excursion));
    }

    // Retrieve all excursions (LiveData for UI updates, real time)
    public LiveData<List<Excursion>> getAllExcursions() {
        return mExcursionDAO.getAllExcursionsLiveData();
    }

    // Get a vacation by ID
    public Vacation getVacationByID(int vacationId) {
        return mVacationDAO.getVacationByID(vacationId);
    }

    // Get vacation title by ID
    public String getVacationTitle(int vacationId) {
        return mVacationDAO.getVacationTitle(vacationId);
    }

    // Get excursion count for a vacation
    public int getExcursionCount(int vacationId) {
        return mVacationDAO.getExcursionCount(vacationId);
    }
}
