package com.example.myapplication.UI.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.UI.database.Repository;
import com.example.myapplication.UI.entities.Vacation;
import com.example.myapplication.UI.entities.Excursion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationViewModel extends AndroidViewModel {
    private Repository repository;
    private ExecutorService executor;

    private MutableLiveData<List<Vacation>> allVacations = new MutableLiveData<>();
    private MutableLiveData<List<Excursion>> allExcursions = new MutableLiveData<>();

    public VacationViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        executor = Executors.newSingleThreadExecutor();

        // Initially fetch vacations and excursions asynchronously
        loadVacationsAndExcursions();
    }

    // Load vacations and excursions asynchronously
    private void loadVacationsAndExcursions() {
        executor.execute(() -> {
            List<Vacation> vacations = repository.getAllVacations();
            List<Excursion> excursions = repository.getAllExcursions();

            allVacations.postValue(vacations != null ? vacations : new ArrayList<>());
            allExcursions.postValue(excursions != null ? excursions : new ArrayList<>());
        });
    }

    // Vacation Methods
    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public Vacation getVacationById(int vacationId) {
        return repository.getVacationByID(vacationId);
    }

    public void insertVacation(Vacation vacation) {
        executor.execute(() -> {
            repository.insertVacation(vacation);
            List<Vacation> updatedVacations = repository.getAllVacations();
            allVacations.postValue(updatedVacations);
        });
    }

    public void updateVacation(Vacation vacation) {
        executor.execute(() -> {
            repository.updateVacation(vacation);
            List<Vacation> updatedVacations = repository.getAllVacations();
            allVacations.postValue(updatedVacations);
        });
    }

    public void deleteVacation(Vacation vacation) {
        executor.execute(() -> {
            repository.deleteVacation(vacation);
            List<Vacation> updatedVacations = repository.getAllVacations();
            allVacations.postValue(updatedVacations);
        });
    }

    // Excursion methods
    public void insertExcursion(Excursion excursion) {
        executor.execute(() -> {
            repository.insertExcursion(excursion);
            List<Excursion> updatedExcursions = repository.getAllExcursions();
            allExcursions.postValue(updatedExcursions);
        });
    }

    public void updateExcursion(Excursion excursion) {
        executor.execute(() -> {
            repository.updateExcursion(excursion);
            List<Excursion> updatedExcursions = repository.getAllExcursions();
            allExcursions.postValue(updatedExcursions);
        });
    }

    public void deleteExcursion(Excursion excursion) {
        executor.execute(() -> {
            repository.deleteExcursion(excursion);
            List<Excursion> updatedExcursions = repository.getAllExcursions();
            allExcursions.postValue(updatedExcursions);
        });
    }

    // Get excursions by vacation ID asynchronously
    public void getExcursionsByVacationId(int vacationId, Callback<List<Excursion>> callback) {
        executor.execute(() -> {
            List<Excursion> excursions = repository.getExcursionsByVacationId(vacationId);
            callback.onResult(excursions);
        });
    }

    // Get a specific excursion by ID asynchronously
    public void getExcursionByID(int excursionId, Callback<Excursion> callback) {
        executor.execute(() -> {
            Excursion excursion = repository.getExcursionByID(excursionId);
            callback.onResult(excursion);
        });
    }

    // Callback interface for asynchronous result handling
    public interface Callback<T> {
        void onResult(T result);
    }

    // Get excursions as LiveData
    public LiveData<List<Excursion>> getAllExcursions() {
        return allExcursions;
    }
}
