package com.example.myapplication.UI.database;

import android.app.Application;

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

    // Retrieve all vacations
    public List<Vacation> getAllVacations() {
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

    // Delete vacation
    public void deleteVacation(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
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

    // Retrieve all excursions
    public List<Excursion> getAllExcursions() {
        return mExcursionDAO.getAllExcursions();
    }

    // Get a vacation by ID
    public Vacation getVacationByID(int vacationId) {
        return mVacationDAO.getVacationByID(vacationId);
    }

    // Get excursion by ID
    public Excursion getExcursionByID(int excursionId) {
        return mExcursionDAO.getExcursionByID(excursionId);
    }

    // Get excursions by vacation ID
    public List<Excursion> getExcursionsByVacationId(int vacationId) {
        return mExcursionDAO.getExcursionsByVacationId(vacationId);
    }
}
